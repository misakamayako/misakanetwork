package cn.com.misakanetwork.tools

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.profile.pegdown.Extensions
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter
import com.vladsch.flexmark.util.ast.Document
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths


fun renderFileToHtml(title: String) {
    val mdFile = Files.readString(Paths.get("${Variable.markdownLocation}$title.md"))
    val file = File("${Variable.htmlLocation}$title.html")
    file.createNewFile()
    val writer = FileWriter(file, false)
    writer.append(PegdownOptions.render(PegdownOptions.parser(mdFile)))
    writer.flush()
}

object PegdownOptions {
    private val OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
        Extensions.ALL
    )
    private val PARSER = Parser.builder(OPTIONS).build()
    fun parser(document:String): Document {
        return PARSER.parse(document)
    }
    private val RENDERER = HtmlRenderer.builder(OPTIONS).build()
    fun render(document: Document):String{
        return RENDERER.render(document)
    }
}
