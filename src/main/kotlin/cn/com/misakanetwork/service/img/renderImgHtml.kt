package cn.com.misakanetwork.service.img

import cn.com.misakanetwork.plugins.database
import org.ktorm.database.asIterable

private data class ImgAndDetail(
    val ImgId: Int,
    val location: String,
    val TagId: String,
    val tagText: String,
    val name: String?,
)

private fun specificImg(id: Int): List<ImgAndDetail> {
    return database.useConnection { conn ->
        val sql = """
        select ref1.id                           as ImgId,
               ref1.location,
               ref1.name                         as name,
               group_concat(DISTINCT it.id)      as TagId,
               group_concat(DISTINCT it.tagText) as tagText
        from img as ref1
                 right join img_to_tag ref2 on ref2.imgId = ref1.id
                 inner join img_tag it on ref2.tagId = it.id
        where ref1.id = ?
        """

        conn.prepareStatement(sql).use { statement ->
            statement.setInt(1, id)
            statement.executeQuery().asIterable().map {
                ImgAndDetail(
                    ImgId = it.getInt("ImgId"),
                    tagText = it.getString("tagText"),
                    TagId = it.getString("TagId"),
                    location = it.getString("location"),
                    name = it.getString("name")
                )
            }
        }
    }
}

