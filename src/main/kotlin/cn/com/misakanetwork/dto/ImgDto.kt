package cn.com.misakanetwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImgTagDTO(val id: Int, val text: String) {
    val TagElement = "/img/tag/$id"
}

@Serializable
data class ImgFolderDTO(val id: Int, val name: String, val tags: List<String>) {
    val ImgFolderElement = "/img/folder/$id"
}

@Serializable
data class ImgDto(val id: Int, val name: String, val belong: Int?, val tags: List<String>) {
    val ImgFolderElement = if (belong == null) {
        null
    } else {
        "/img/folder/$belong"
    }
}
