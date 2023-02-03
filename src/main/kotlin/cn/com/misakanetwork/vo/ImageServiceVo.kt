package cn.com.misakanetwork.vo

import cn.com.misakanetwork.dto.*

interface ImageServiceVo {
	suspend fun createAlbum(createAlbumDTO: CreateAlbumDTO): AlbumDTO
	suspend fun updateAlbum(updateAlbumDTO: UpdateAlbumDTO): AlbumDTO
	suspend fun getAlbumList(albumQueryDTO: AlbumQueryDTO): PaginationDTO<List<AlbumDTO>>
	suspend fun getAlbum(albumId: Int): AlbumDTO
	suspend fun getImgList(page: Int? = 1, pageSize: Int? = 10, category: Int? = null): PaginationDTO<List<ImgDTO>>
	suspend fun getImgDetail(eigenvalues: String): ImgDetailDTO
	suspend fun uploadImg(imgUploadDTO: ImgUploadDTO): ImgDetailDTO
	suspend fun updateImg(updateImgDTO: UpdateImgDTO): ImgDetailDTO
}
