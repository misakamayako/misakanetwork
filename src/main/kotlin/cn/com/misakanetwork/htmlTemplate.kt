package cn.com.misakanetwork

import cn.com.misakanetwork.dao.ImgTagsDAO
import cn.com.misakanetwork.dto.ImgTagDTO
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.io.ByteArrayOutputStream
import java.rmi.ServerException

class Layout : Template<HTML> {
    val keywords = Placeholder<HEAD>()
    val description = Placeholder<HEAD>()
    val content = TemplatePlaceholder<ContentTemplate>()

    override fun HTML.apply() {
        lang = "zh-hans"
        head {
            meta(charset = "UTF-8")
            meta(name = "viewport",
                content = "width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no")
            insert(keywords)
            insert(description)
            link(href = "https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css", rel = "stylesheet")
        }
        body {
            classes = setOf("h-screen", "w-screen", "bg-gray-100")
            insert(ContentTemplate(), content)
        }
    }
}

class ContentTemplate : Template<FlowContent> {
    val anyThing = Placeholder<FlowContent>()
    override fun FlowContent.apply() {
        insert(anyThing)
    }
}

class ImgFiles : Template<FlowContent> {
    //    val contentClass = ;
    val mainImage = TemplatePlaceholder<MainImage>()
    val tags = PlaceholderList<DIV, FlowContent>()
    override fun FlowContent.apply() {
        div {
            classes = setOf("container", "grid", "grid-cols-12", "gap-4", "h-full")
            div {
                classes = setOf("col-span-8", "h-full", "flex", "flex-col", "justify-center", "bg-stone-200")
                insert(MainImage(), mainImage)
            }
            div {
                classes = setOf("col-span-4", "py-2", "px-4")
                div {
                    classes = setOf("text-lg", "font-bold")
                    +"title"
                }
                hr {
                    classes = setOf("mb-1")
                }
                if (!tags.isEmpty()) {
                    each(tags) {
                        insert(it)
                    }
                }
            }
        }
    }
}

class MainImage() : Template<FlowContent> {
    val imgSrc = Placeholder<String>()
    override fun FlowContent.apply() {
        img {
            src = imgSrc.toString()
            classes = setOf("cursor-pointer")
        }
    }
}

class ImgTags(private val tagInfo: ImgTagDTO) : Template<FlowContent> {
    override fun FlowContent.apply() {
        div {
            classes = setOf("border",
                "border-lime-500",
                "rounded-lg",
                "inline",
                "px-2",
                "hover:border-lime-300",
                "cursor-pointer")
            tagInfo.text
        }
    }
}
