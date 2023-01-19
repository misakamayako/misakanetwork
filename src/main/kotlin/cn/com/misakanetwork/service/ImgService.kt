package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.AlbumDAO
import cn.com.misakanetwork.dto.*
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.vo.ImageServiceVo
import org.ktorm.database.asIterable
import org.ktorm.dsl.insert

class ImgService : ImageServiceVo {
	override suspend fun createAlbum(createAlbumDTO: CreateAlbumDTO): AlbumDTO {
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
			if (id != -1) {
				return getAlbum(id)
			} else {
				throw UnknownError()
			}
		} catch (e: Error) {
			throw InternalError(e.message)
		}
	}

	override suspend fun updateAlbum(updateAlbumDTO: UpdateAlbumDTO): AlbumDTO {
		TODO("Not yet implemented")
	}

	override suspend fun getAlbumList(page: Int?, pageSize: Int?): PaginationDTO<List<AlbumDTO>> {
		TODO("Not yet implemented")
	}

	override suspend fun getAlbum(albumId: Int): AlbumDTO {
		TODO("Not yet implemented")
	}

	override suspend fun getImgList(page: Int?, pageSize: Int?, category: Int?): PaginationDTO<List<ImgDTO>> {
		TODO("Not yet implemented")
	}

	override suspend fun getImgDetail(eigenvalues: String): ImgDetailDTO {
		TODO("Not yet implemented")
	}

	override suspend fun uploadImg(imgUploadDTO: ImgUploadDTO): ImgDetailDTO {
		TODO("Not yet implemented")
	}

	override suspend fun updateImg(updateImgDTO: UpdateImgDTO): ImgDetailDTO {
		TODO("Not yet implemented")
	}
}
