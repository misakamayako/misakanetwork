package cn.com.misakanetwork.service.article

import cn.com.misakanetwork.dao.ArticleDAO
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.tools.Variable
import cn.com.misakanetwork.tools.renderFileToHtml
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import org.ktorm.dsl.*
import java.io.File

class ArticleReader(private val call: ApplicationCall) {
    suspend fun getOrRenderFile(id: Int?) {
        if (id == null) {
            throw BadRequestException("article id is not valid number")
        }
        val title = getTitle(id) ?: throw NotFoundException()
        var file = File("${Variable.htmlLocation}$title.html")
        if (!file.exists()) {
            renderFileToHtml(title)
            file = File("${Variable.htmlLocation}$title.html")
        }
        call.respondFile(file)
    }

    private fun getTitle(id: Int): String? {
        val query = database.from(ArticleDAO).select().where { ArticleDAO.id eq id }
        if (query.totalRecords != 1) {
            return null
        }
        for (row in query) {
            return row[ArticleDAO.title]
        }
        return null
    }
}
