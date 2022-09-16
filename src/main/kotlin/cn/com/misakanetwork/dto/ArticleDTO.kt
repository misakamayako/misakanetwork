package cn.com.misakanetwork.dto

import cn.com.misakanetwork.tools.LocalDateSerializer
import kotlinx.serialization.*
import java.time.LocalDateTime

interface IArticleDTO {
    val id: Int?
    val title: String?
    val brief: String?

    @Serializable(with = LocalDateSerializer::class)
    val createAt: LocalDateTime?
    val views: Int?
}

@Serializable
data class ArticleDTO(
    override val id: Int?,
    override val title: String?,
    override val brief: String?,
    @Serializable(with = LocalDateSerializer::class)
    override val createAt: LocalDateTime?,
    override val views: Int?,
) : IArticleDTO

@Serializable
data class ArticleDetailDTO(
    override val id: Int?,
    override val title: String?,
    override val brief: String?,
    @Serializable(with = LocalDateSerializer::class)
    override val createAt: LocalDateTime?,
    override val views: Int?,
    var content: String?,
) : IArticleDTO

@Serializable
data class ArticleAllDTO(
    val id: Int?,
    val title: String?,
)
