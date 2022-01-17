package cn.com.misakanetwork.service.img

import cn.com.misakanetwork.ImgFiles
import cn.com.misakanetwork.ImgTags
import cn.com.misakanetwork.dto.ImgTagDTO
import cn.com.misakanetwork.plugins.database
import io.ktor.features.*
import org.ktorm.database.asIterable
import java.util.*

private data class ImgAndDetail(val ImgId: Int, val location: String, val TagId: String, val tagText: String,val name: String?)

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

fun renderImgHtml(id: Int): ImgFiles {
    val imgInfo = specificImg(id).getOrNull(0)?:throw NotFoundException()
    val imgTagDTOs = LinkedList<ImgTags>()
    val tags = imgInfo.tagText.split(',')
    val ids = imgInfo.TagId.split(',')
    for (i in tags.indices) {
        imgTagDTOs.add(ImgTags(ImgTagDTO(ids[i].toInt(), tags[i])))
    }
    return ImgFiles(imgInfo.location, imgTagDTOs, imgInfo.name)
}

