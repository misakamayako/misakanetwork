package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.ArticleDAO
import cn.com.misakanetwork.dao.ArticleToCategoryDAO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.enum.OSSInfo
import cn.com.misakanetwork.plugins.OSSInstance
import cn.com.misakanetwork.plugins.database
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.*
import org.ktorm.database.asIterable
import org.ktorm.dsl.batchInsert
import org.ktorm.dsl.insert
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.time.LocalDateTime

class ArticleService(call: ApplicationCall) : Service(call) {
    private val logger: Logger = LoggerFactory.getLogger("cn.com.misakanetwork.service.ArticleService")
    suspend fun getBrief(page: Int) {
        this.javaClass.packageName
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun upload() {
        /*val form = call.receiveMultipart()
        var title = ""
        var categories = emptyList<String>()
        var fileBytes: ByteArray? = null
        form.forEachPart {
            when (it) {
                is PartData.FileItem -> {
                    fileBytes = it.streamProvider().readBytes()
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
        if (fileBytes == null) {
            throw BadRequestException("please upload a file")
        }
        val ossJob = GlobalScope.launch {
            OSSInstance.putObject(OSSInfo.MARKDOWN.bucket, title, ByteArrayInputStream(fileBytes))
        }
        val sqlJob = GlobalScope.launch {
            val ids = ArrayList<Int>()
            database.useTransaction {
                if (categories.isNotEmpty()) {
                    //插入类型，存在则忽略
                    database.useConnection { connection ->
                        val sql = StringBuilder("insert ignore into article_category (description) values ")
                        categories.forEach { category ->
                            sql.append("('$category'),")
                        }
                        sql.deleteCharAt(sql.length - 1)
                        connection.prepareStatement(sql.toString().also { database.logger.info(it) }).use { resource ->
                            resource.execute()
                        }
                    }
                    //获取类型id
                    database.useConnection { connection ->
                        val sql = "select id " +
                                "from article_category " +
                                "where description in ('${categories.joinToString("','")}')"
                        connection.prepareStatement(sql.also { database.logger.info(it) }).use { resource ->
                            resource.executeQuery().asIterable().forEach { resultSet ->
                                ids.add(resultSet.getInt(1))
                            }
                        }
                    }
                }
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
                        resource.executeQuery().asIterable().forEach {
                            articleId = it.getInt(1)
                        }
                    }
                }
                database.batchInsert(ArticleToCategoryDAO) {
                    ids.forEach { id ->
                        item {
                            set(ArticleToCategoryDAO.category, id)
                            set(ArticleToCategoryDAO.article, articleId)
                        }
                    }
                }
                it.commit()
            }
        }
        withTimeoutOrNull(5000) {
            listOf(ossJob, sqlJob).joinAll()
        }*/
        this.response(HttpStatusCode.OK, "ok")
//        call.respond(ResponseDTO(data="Ok"))
    }
}

