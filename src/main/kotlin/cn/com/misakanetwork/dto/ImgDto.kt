package cn.com.misakanetwork.dto

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AlbumDTO(
	val id: Int,
	val title: String,
	val cover: String?,
	val category: List<CategoryDTO>?,
	@Transient
	val nsfw: Boolean = false,
	@Transient
	val private: Boolean = false
)

@Serializable
data class CreateAlbumDTO(
	@Required
	val title: String,
	val categories: List<Int>,
	@Transient
	val nsfw: Boolean = false,
	@Transient
	val private: Boolean = false
)

@Serializable
data class UpdateAlbumDTO(
	@Required
	val id: Int,
	val title: String?,
	val categories: List<Int>?,
	val nsfw: Boolean?,
	val private: Boolean?,
)

@Serializable
data class ImgDTO(
	@Transient
	val id: Int = -1,
	val url: String,
	val name: String,
	val category: List<CategoryDTO>?,
)

@Serializable
data class ImgDetailDTO(
	@Transient
	val id: Int = -1,
	@Transient
	val eigenvalues: String = "",
	val url: String,
	val name: String,
	val category: List<CategoryDTO>,
	val album: AlbumDTO?,
	val nsfw: Boolean = false,
	val private: Boolean = false
)

@Serializable
data class ImgUploadDTO(
	@Required
	val fileUrl: String,
	val name: String,
	val categories: List<Int>? = null,
	val album: Int? = null,
	val nsfw: Boolean = false,
	val private: Boolean = false
)

@Serializable
data class UpdateImgDTO(
	@Required
	val id: Int,
	val fileUrl: String?,
	val categories: List<Int>?,
	val album: Int?,
	@Transient
	val nsfw: Boolean? = false,
	@Transient
	val private: Boolean? = false
)

@Serializable
data class AlbumQueryDTO(
	val page: Int,
	val pageSize: Int,
	val keyword: String?,
	val category: List<Int>?,
	@Transient
	val showNsfw: Boolean? = false,
	@Transient
	val showPrivate: Boolean? = false
)

@Serializable
data class AlbumViewSetting(
	val nsfw: Boolean? = null,
	val private: Boolean? = null
)

@Serializable
data class AlbumWithImgList(
	@Transient
	val id: Int = -1,
	val title: String,
	val cover: String?,
	val category: List<CategoryDTO>?,
	var imgList:List<ImgDTO>? = null
)
