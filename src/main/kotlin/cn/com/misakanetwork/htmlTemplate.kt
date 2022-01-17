package cn.com.misakanetwork

import cn.com.misakanetwork.dto.ImgTagDTO
import io.ktor.html.*
import kotlinx.html.*

class Layout<T : Template<FlowContent>>(private val template: T) : Template<HTML> {
    val keywords = Placeholder<HEAD>()
    val description = Placeholder<HEAD>()
    val content = TemplatePlaceholder<T>()
    val title = Placeholder<TITLE>()

    override fun HTML.apply() {
        lang = "zh-hans"
        head {
            title
            meta(charset = "UTF-8")
            meta(name = "viewport",
                content = "width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no")
            insert(keywords)
            insert(description)
            link(href = "https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css", rel = "stylesheet")
        }
        body {
            classes = setOf("h-screen", "w-screen", "bg-gray-100")
            insert(template, content)
        }
    }
}

class ImgFiles(private val source: String, private val tags: List<ImgTags>,val title:String?) : Template<FlowContent> {
    val mainImage = TemplatePlaceholder<MainImage>()
    val tagBlock = TemplatePlaceholder<ImgTags>()
    override fun FlowContent.apply() {
        div {
            classes = setOf("container", "grid", "grid-cols-12", "gap-4", "h-full")
            div {
                classes = setOf("col-span-8", "h-full", "flex", "flex-col", "justify-center", "bg-stone-200")
                insert(MainImage(source), mainImage)
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
                for (tag in tags) {
                    insert(tag, tagBlock)
                }
            }
        }
    }
}

class MainImage(val source: String) : Template<FlowContent> {
    override fun FlowContent.apply() {
        img {
            src = source
            classes = setOf("cursor-pointer")
        }
    }
}

class ImgTags(val tagInfo: ImgTagDTO) : Template<FlowContent> {
    override fun FlowContent.apply() {
        a {
            href  = "/img/tags/?"
            classes = setOf("border",
                "border-lime-500",
                "rounded-lg",
                "inline",
                "px-2",
                "hover:border-lime-300",
                "cursor-pointer")
            +tagInfo.text
        }
    }
}


class HTMLGroupByTagId:Template<FlowContent>{
    override fun FlowContent.apply() {
        TODO("Not yet implemented")
    }
}

class HTMLImgUpload:Template<FlowContent>{
    override fun FlowContent.apply() {
        TODO("Not yet implemented")
    }
}
