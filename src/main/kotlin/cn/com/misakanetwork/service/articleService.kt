package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.ArticleDAO
import cn.com.misakanetwork.dao.ArticleToCategoryDAO
import cn.com.misakanetwork.dto.ArticleDTO
import cn.com.misakanetwork.dto.ArticleDetailDTO
import cn.com.misakanetwork.dto.PaginationDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.enum.OSSInfo
import cn.com.misakanetwork.plugins.OSSInstance
import cn.com.misakanetwork.plugins.database
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.ktorm.database.asIterable
import org.ktorm.dsl.*
import java.io.ByteArrayInputStream
import java.time.LocalDateTime

class ArticleService(private val call: ApplicationCall) {
    suspend fun getArticleList(page: Int) {
        val result = ArrayList<ArticleDTO>()

        var total = 0
        database.useTransaction { transaction ->
            database.from(ArticleDAO).select().limit(10).offset((page - 1) * 10).map {
                result.add(ArticleDTO(
                    id = it[ArticleDAO.id],
                    title = it[ArticleDAO.title],
                    brief = it[ArticleDAO.brief],
                    createAt = it[ArticleDAO.createAt],
                    views = it[ArticleDAO.views]
                ))
            }
            database.useConnection { connection ->
                connection.prepareStatement("SELECT FOUND_ROWS() as total;").use { statement ->
                    for (resultSet in statement.executeQuery().asIterable()) {
                        total = resultSet.getInt(1)
                        break
                    }
                }
            }
            transaction.commit()
        }
        call.respond(ResponseDTO(data = PaginationDTO(total = total, page = page, pageSize = 10, data = result)))
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun upload() {
        val form = call.receiveMultipart()
        var title = ""
        var categories = emptyList<String>()
        var fileBytes: ByteArray? = null
        val imgList = ArrayList<ByteArray>()
        form.forEachPart {
            when (it) {
                is PartData.FileItem -> {
                    when (it.name) {
                        "file" -> fileBytes = it.streamProvider().readBytes()
                        "img" -> imgList.add(it.streamProvider().readBytes())
                    }
                }
                is PartData.FormItem -> {
                    when (it.name) {
                        "title" -> title = it.value
                        "categories" -> categories = it.value.split(",")
                    }
                }
                else -> {}
            }
        }
        if (fileBytes?.isNotEmpty() != true) {
            throw BadRequestException("please upload a file")
        }
        val ossJob = GlobalScope.launch {
            OSSInstance.putObject(OSSInfo.MARKDOWN.bucket, title, ByteArrayInputStream(fileBytes))
        }
        val sqlJob = GlobalScope.launch {
            database.useTransaction { transaction ->
                //插入文章记录
                database.insert(ArticleDAO) {
                    val brief = String(ByteArrayInputStream(fileBytes as ByteArray).readNBytes(120))
                    set(ArticleDAO.title, title)
                    set(ArticleDAO.brief, brief)
                    set(ArticleDAO.createAt, LocalDateTime.now())
                }
                var articleId = 0
                database.useConnection { connection ->
                    connection.prepareStatement("select last_insert_id()").use { resource ->
                        resource.executeQuery().asIterable().forEach { resultSet ->
                            articleId = resultSet.getInt(1)
                        }
                    }
                }
                database.batchInsert(ArticleToCategoryDAO) {
                    categories.forEach { id ->
                        item {
                            set(ArticleToCategoryDAO.category, id.toInt())
                            set(ArticleToCategoryDAO.article, articleId)
                        }
                    }
                }
                transaction.commit()
            }
        }
        val result = withTimeoutOrNull(5000) {
            ossJob.join()
            sqlJob.join()
            "Done"
        }
        if (result == "Done" && !ossJob.isCancelled && !sqlJob.isCancelled) {
            call.respond(ResponseDTO(data = "Ok"))
        } else {
            var message = ""
            if (ossJob.isCancelled) {
                message = "ossJob filed"
            }
            if (sqlJob.isCancelled) {
                message += "sqlJob filed"
            }
            throw InternalError(message)
        }
    }

    suspend fun getArticle(id: Int) {
        val res = database.from(ArticleDAO).select().where { ArticleDAO.id eq id }.map {
            ArticleDetailDTO(it[ArticleDAO.id],
                             it[ArticleDAO.title],
                             it[ArticleDAO.brief],
                             it[ArticleDAO.createAt],
                             it[ArticleDAO.views],
                             null)
        }
        if (res.isEmpty()) {
            throw NotFoundException()
        }
        val item = res[0]
        val ossObject = OSSInstance.getObject(OSSInfo.MARKDOWN.bucket, item.title)
        item.content = String(ossObject.objectContent.readBytes())
        call.respond(ResponseDTO(data = item))
    }
}
