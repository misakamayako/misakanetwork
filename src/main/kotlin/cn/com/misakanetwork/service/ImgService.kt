package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.AlbumDAO
import cn.com.misakanetwork.dao.CategoryDAO
import cn.com.misakanetwork.dao.ImagesDAO
import cn.com.misakanetwork.dao.ImagesToAlbumDAO
import cn.com.misakanetwork.dto.*
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.plugins.groupConcat
import cn.com.misakanetwork.vo.ImageServiceVo
import org.ktorm.database.asIterable
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring

class ImgService : ImageServiceVo {
	override suspend fun createAlbum(createAlbumDTO: CreateAlbumDTO) {
		try {
			database.insert(AlbumDAO) {
				set(it.title, createAlbumDTO.title)
				set(it.nsfw, createAlbumDTO.nsfw)
				set(it.private, createAlbumDTO.private)
			}
			var id: Int = -1
			database.useConnection { connection ->
				connection.prepareStatement("select last_insert_id()").use { resource ->
					resource.executeQuery().asIterable().forEach { resultSet ->
						id = resultSet.getInt(1)
					}
				}
			}
			if (id == -1) {
				throw UnknownError()
			}
		} catch (e: Error) {
			throw InternalError(e.message)
		}
	}

	override suspend fun updateAlbum(updateAlbumDTO: UpdateAlbumDTO): AlbumDTO {
		TODO("Not yet implemented")
	}

	override suspend fun getAlbumList(albumQueryDTO: AlbumQueryDTO): PaginationDTO<List<AlbumDTO>> {
		val list = albumBaseSql
			.where {
				val arrayList = ArrayList<ColumnDeclaring<Boolean>>()
				arrayList += AlbumDAO.nsfw eq (albumQueryDTO.showNsfw == true)
				arrayList += AlbumDAO.private eq (albumQueryDTO.showPrivate == true)
				if (!albumQueryDTO.category.isNullOrEmpty()) {
					arrayList += CategoryDAO.id inList albumQueryDTO.category
				}
				if (albumQueryDTO.keyword != null) {
					arrayList += AlbumDAO.title like "%${albumQueryDTO.keyword}%"
				}
				arrayList.reduce { acc, columnDeclaring -> acc and columnDeclaring }
			}
			.limit((albumQueryDTO.page - 1) * albumQueryDTO.pageSize, albumQueryDTO.pageSize)
			.map {
				val category = getCategory(it)
				AlbumDTO(
					id = it[AlbumDAO.id]!!,
					title = it[AlbumDAO.title]!!,
					cover = it[AlbumDAO.cover],
					category = category
				)
			}
		val total = database.from(AlbumDAO)
			.leftJoin(CategoryDAO, (CategoryDAO.id eq AlbumDAO.id) and (CategoryDAO.type eq 2))
			.select(count())
			.groupBy(AlbumDAO.id)
			.where {
				val arrayList = ArrayList<ColumnDeclaring<Boolean>>()
				arrayList += AlbumDAO.nsfw eq (albumQueryDTO.showNsfw == true)
				arrayList += AlbumDAO.private eq (albumQueryDTO.showPrivate == true)
				if (albumQueryDTO.category != null) {
					arrayList += CategoryDAO.id inList albumQueryDTO.category
				}
				arrayList.reduce { a, b -> a and b }
			}
			.map {
				it.getInt(1)
			}
		return PaginationDTO(
			total = total.firstOrNull() ?: 0,
			page = albumQueryDTO.page,
			pageSize = albumQueryDTO.pageSize,
			data = list
		)
	}

	override suspend fun getAlbum(albumId: Int): AlbumWithImgList? {
		val albumWithImgList = albumBaseSql.where {
			AlbumDAO.id eq albumId
		}.map {
			val category = getCategory(it)
			AlbumWithImgList(
				id = it[AlbumDAO.id]!!,
				title = it[AlbumDAO.title]!!,
				cover = it[AlbumDAO.cover],
				category = category
			)
		}.getOrNull(0) ?: return null
		albumWithImgList.imgList = imgBaseSql.where {
			ImagesDAO.album eq albumId
		}.let(::getImgList)
		return albumWithImgList
	}

	override suspend fun getImgList(
		page: Int,
		pageSize: Int,
		tags: List<Int>?,
		showPrivate: Boolean,
		nsfw: Boolean
	): PaginationDTO<List<ImgDTO>> {
		val query = imgBaseSql
			.where {
				val arrayList = ArrayList<ColumnDeclaring<Boolean>>()
				arrayList += ImagesDAO.private eq showPrivate
				arrayList += ImagesDAO.nsfw eq nsfw
				if (tags != null) {
					arrayList += CategoryDAO.id inList tags
				}
				arrayList.reduce { a, b -> a and b }
			}.let(::getImgList)
		return PaginationDTO(total = 10, page = 1, pageSize = 1, data = query)
	}

	override suspend fun getImgDetail(eigenvalues: String): ImgDetailDTO {
		TODO("Not yet implemented")
	}

	override suspend fun uploadImg(imgUploadDTO: ImgUploadDTO): ImgDetailDTO {
		database.insert(ImagesDAO) {
			set(ImagesDAO.eigenvalues, imgUploadDTO.fileUrl)
			set(ImagesDAO.name, imgUploadDTO.name)
			set(ImagesDAO.nsfw, imgUploadDTO.nsfw)
			set(ImagesDAO.album, imgUploadDTO.album)
			set(ImagesDAO.nsfw, imgUploadDTO.nsfw)
			set(ImagesDAO.private, imgUploadDTO.private)
		}
		if (!imgUploadDTO.categories.isNullOrEmpty()) {
			val id = database.from(ImagesDAO).select(ImagesDAO.id).where {
				ImagesDAO.eigenvalues eq imgUploadDTO.fileUrl
			}.map { it[ImagesDAO.id] }.firstOrNull() ?: throw InternalError("数据库更新失败")
			database.batchInsert(ImagesToAlbumDAO) {
				imgUploadDTO.categories.map {
					item {

					}
				}
			}

		}
		TODO("")
	}

	override suspend fun updateImg(updateImgDTO: UpdateImgDTO): ImgDetailDTO {
		TODO("Not yet implemented")
	}

	override suspend fun deleteAlbum(id: Int) {
		TODO("Not yet implemented")
	}

	private fun getCategory(queryRowSet: QueryRowSet) = if (queryRowSet.getString("categoryIds") != null) {
		val a = queryRowSet.getString("categories")!!.split(",")
		val b = queryRowSet.getString("categoryIds")!!.split(",")
		List(a.size) { index -> CategoryDTO(a[index], b[index].toInt(), 2) }
	} else {
		null
	}

	private val albumBaseSql: Query
		get() {
			return database.from(AlbumDAO)
				.leftJoin(CategoryDAO, (CategoryDAO.id eq AlbumDAO.id) and (CategoryDAO.type eq 2))
				.select(
					AlbumDAO.id,
					AlbumDAO.title,
					AlbumDAO.cover,
					AlbumDAO.nsfw,
					AlbumDAO.private,
					CategoryDAO.id,
					groupConcat(CategoryDAO.description, ",").aliased("categories"),
					groupConcat(CategoryDAO.id, ",").aliased("categoryIds"),
				)
				.groupBy(AlbumDAO.id)
		}
	private val imgBaseSql: Query
		get() {
			return database
				.from(ImagesDAO)
				.leftJoin(AlbumDAO, ImagesDAO.album eq AlbumDAO.id)
				.leftJoin(CategoryDAO, (CategoryDAO.id eq ImagesDAO.id) and (CategoryDAO.type eq 2))
				.select(
					ImagesDAO.id,
					ImagesDAO.eigenvalues,
					ImagesDAO.name,
					ImagesDAO.nsfw,
					ImagesDAO.private,
					AlbumDAO.id.aliased("album_id"),
					AlbumDAO.title.aliased("album_title"),
					groupConcat(CategoryDAO.description, ",").aliased("categories"),
					groupConcat(CategoryDAO.id, ",").aliased("categoryIds"),
				)
				.groupBy(ImagesDAO.id)
		}
	private fun getImgList(query:Query):List<ImgDTO>{
		return query.map {
			val categoryIds = it.getArray("categoryIds")?.array as Array<*>?
			val categories = it.getArray("categories")?.array as Array<*>?
			val categoryDTOList = if (categoryIds != null && categories != null) {
				List(categoryIds.size) { index ->
					CategoryDTO(
						categories[index].toString(),
						categoryIds[index].toString().toInt(),
						2
					)
				}
			} else {
				null
			}
			ImgDTO(
				it[ImagesDAO.id]!!,
				it[ImagesDAO.eigenvalues]!!,
				it[ImagesDAO.name]!!,
				categoryDTOList
			)
		}
	}
}
