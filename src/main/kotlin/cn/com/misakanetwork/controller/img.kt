package cn.com.misakanetwork.controller

//import cn.com.misakanetwork.Layout
import cn.com.misakanetwork.ImgFiles
import cn.com.misakanetwork.Layout
import cn.com.misakanetwork.service.img.getImgTags
import cn.com.misakanetwork.service.img.imgUploader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.routing.*

fun imgController(app: Application) {
    app.routing {
        get<ImgReader.Random> {

        }
        get<ImgReader.Search> {

        }
        get<ImgReader.SpecificImg> {
            call.respondHtmlTemplate(Layout()){
                content{
                    anyThing{
                        TODO("img element")
                    }
                }
            }
        }
        get<ImgReader.ImgTags> {
            call.respondHtml { getImgTags(it.keyWord) }
        }
        authenticate("auth-jwt") {
            get<ImgReader.Upload> {
                call.respondHtml { imgUploader() }
            }
        }
    }
}

@Location("/img")
private class ImgReader {
    @Location("/random")
    class Random

    @Location("/search")
    data class Search(val page: Int, val count: Int, val text: String?)

    @Location("/tags")
    data class ImgTags(val keyWord: String?)

    @Location("/upload")
    class Upload

    @Location("/{id}")
    data class SpecificImg(val id: Int)
}
