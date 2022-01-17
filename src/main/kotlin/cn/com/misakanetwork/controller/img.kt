package cn.com.misakanetwork.controller

import cn.com.misakanetwork.Layout
import cn.com.misakanetwork.service.img.getImgTags
import cn.com.misakanetwork.service.img.imgUploader
import cn.com.misakanetwork.service.img.renderHTMLGroupByTagId
import cn.com.misakanetwork.service.img.renderImgHtml
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.routing.*

fun imgController(app: Application) {
    app.routing {
        get<ImgReader.HTMLRandom> {

        }
        get<ImgReader.HTMLSearchImg> {

        }
        get<ImgReader.HTMLGroupByTagId> {
            val contentPage = renderHTMLGroupByTagId(it)
            call.respondHtmlTemplate(Layout(contentPage)){
                title{
                    +"content."
                }
                content
            }
        }
        get<ImgReader.HTMLSpecificImg> {
            val contentPage = renderImgHtml(it.id)
            call.respondHtmlTemplate(Layout(contentPage)) {
                title{
                    +("查看图片  " + (contentPage.title ?: ""))
                }
                content {
                    mainImage
                }
            }
        }
        get<ImgReader.JSONImgTags> {
            call.respondHtml { getImgTags(it.keyWord) }
        }
        authenticate("auth-jwt") {
            get<ImgReader.HTMLImgUpload> {
                call.respondHtml { imgUploader() }
            }
        }
    }
}

@Location("/img")
class ImgReader {
    @Location("/random")
    class HTMLRandom

    @Location("/search")
    data class HTMLSearchImg(val page: Int, val count: Int, val text: String?)

    @Location("/tags")
    data class JSONImgTags(val keyWord: String?, val page: Int?)

    @Location("/tags/{id}")
    data class HTMLGroupByTagId(val id: Int)

    @Location("/upload")
    class HTMLImgUpload

    @Location("/{id}")
    data class HTMLSpecificImg(val id: Int)
}
