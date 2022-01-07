package cn.com.misakanetwork.service.img

import cn.com.misakanetwork.ImgFile
import cn.com.misakanetwork.plugins.database
import io.ktor.features.*
import org.ktorm.database.asIterable

data class ImgAndDetail(val ImgId: Int, val tagText: String, val location: String)

fun specificImg(id: Int): List<ImgAndDetail> {
    return database.useConnection { conn ->
        val sql = """
        select ref1.id                           as ImgId,
               ref1.location,
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
                    location = it.getString("location"),
                )
            }
        }
    }
}

//fun renderImgHtml(id:Int): ImgFile {
//    val imgInfo = specificImg(id)
//    if (imgInfo.isEmpty()){
//        throw NotFoundException()
//    } else{
//        return ImgFile(imgInfo[0].location,imgInfo[0].tagText)
//    }
//}
