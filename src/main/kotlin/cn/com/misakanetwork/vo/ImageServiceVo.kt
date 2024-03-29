package cn.com.misakanetwork.vo

import cn.com.misakanetwork.dto.*
import io.ktor.server.request.*

interface ImageServiceVo {
	suspend fun createAlbum(createAlbumDTO: CreateAlbumDTO)
	suspend fun updateAlbum(updateAlbumDTO: UpdateAlbumDTO): AlbumDTO
	suspend fun getAlbumList(albumQueryDTO: AlbumQueryDTO): PaginationDTO<List<AlbumDTO>>
	suspend fun getAlbum(albumId: Int): AlbumWithImgList?
	suspend fun getAlbumList():List<Int>

	fun ableToAccessAlbum(albumId:Int, cookie: RequestCookies):Unit
	suspend fun getImgList(
		page: Int,
		pageSize: Int,
		tags: List<Int>?,
		showPrivate: Boolean,
		nsfw: Boolean
	): PaginationDTO<List<ImgDTO>>

	suspend fun getImgDetail(eigenvalues: String): ImgDetailDTO
	suspend fun uploadImg(imgUploadDTO: ImgUploadDTO): Unit
	suspend fun updateImg(updateImgDTO: UpdateImgDTO): ImgDetailDTO
	suspend fun deleteAlbum(id: Int): Unit
}
