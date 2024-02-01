package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.ArticleDAO
import cn.com.misakanetwork.dao.ArticleToCategoryDAO
import cn.com.misakanetwork.dao.CategoryDAO
import cn.com.misakanetwork.dto.*
import cn.com.misakanetwork.enum.OSSInfo
import cn.com.misakanetwork.plugins.OSSInstance
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.plugins.groupConcat
import cn.com.misakanetwork.vo.ArticleUploadVO
import cn.com.misakanetwork.vo.ArticleVO
import com.aliyun.oss.OSSException
import io.ktor.server.plugins.*
import kotlinx.coroutines.*
import org.ktorm.database.asIterable
import org.ktorm.dsl.*
import java.io.ByteArrayInputStream
import java.time.LocalDateTime


class ArticleService : ArticleVO {
    override fun getDefaultQuery(): Query {
        val a = ArticleDAO
        val atc = ArticleToCategoryDAO
        val ac = CategoryDAO
        return database.from(a)
            .leftJoin(atc, a.id eq atc.article)
            .leftJoin(ac, ac.id eq atc.category)
            .select(
                a.id,
                a.brief,
                a.title,
                a.views,
                a.createAt,
                groupConcat(ac.description).aliased("descriptions"),
                groupConcat(atc.category).aliased("categories"),
            )
            .groupBy(a.id)
            .orderBy(a.id.desc())
    }

    override fun getByCategory(): Query {
        val atc = ArticleToCategoryDAO
        val atc2 = ArticleToCategoryDAO("atc2")
        val a = ArticleDAO
        val ac = CategoryDAO
        return database.from(atc)
            .leftJoin(atc2, atc2.article eq atc.article)
            .leftJoin(a, a.id eq atc.article)
            .leftJoin(ac, ac.id eq atc2.category)
            .select(
                a.id,
                a.brief,
                a.title,
                a.views,
                a.createAt,
                groupConcat(ac.description).aliased("descriptions"),
                groupConcat(atc.category).aliased("categories"),
            )
            .groupBy(atc.article)
            .orderBy(a.id.desc())
    }

    override fun getResult(query: Query): List<ArticleDTO> {
        return query.map {
            val descriptions = it.getString("descriptions")?.split(",")
            val categories = it.getString("categories")?.split(",")?.map { category -> category.toInt() }
            ArticleDTO(
                id = it[ArticleDAO.id],
                title = it[ArticleDAO.title],
                brief = it[ArticleDAO.brief],
                createAt = it[ArticleDAO.createAt],
                views = it[ArticleDAO.views],
                categories = if (descriptions != null && categories != null) {
                    List(descriptions.size) { index -> CategoryDTO(descriptions[index], categories[index], 1) }
                } else {
                    null
                }
            )
        }
    }

    override suspend fun getArticleList(page: Int, pageSize: Int, category: Int?): PaginationDTO<List<ArticleDTO>> {
        val query = if (category == null) {
            getDefaultQuery()
        } else {
            val atc = ArticleToCategoryDAO
            getByCategory().where { atc.category eq category }
        }
        val result = getResult(query.limit((page - 1) * pageSize, pageSize))
        val total = if (category == null) {
            database.from(ArticleDAO).select(count(ArticleDAO.id)).map { it.getInt(1) }[0]
        } else {
            database
                .from(ArticleDAO)
                .rightJoin(ArticleToCategoryDAO, ArticleToCategoryDAO.article eq ArticleDAO.id)
                .select(countDistinct(ArticleDAO.id))
                .where { ArticleToCategoryDAO.category eq category }.map { it.getInt(1) }[0]
        }
        return PaginationDTO(total, page, 10, result)
    }

    override suspend fun uploadArticle(articleUploadVO: ArticleUploadVO)=coroutineScope {
        val ossJob = launch {
            OSSInstance.putObject(
                OSSInfo.MARKDOWN.bucket,
                articleUploadVO.title + ".md",
                ByteArrayInputStream(articleUploadVO.content.toByteArray())
            )
        }
        var articleId = 0
        val sqlJob = launch {
            database.useTransaction { transaction ->
                database.insert(ArticleDAO) {
                    val brief = articleUploadVO.content.substring(0, minOf(120, articleUploadVO.content.length))
                    set(ArticleDAO.title, articleUploadVO.title)
                    set(ArticleDAO.brief, brief)
                    set(ArticleDAO.createAt, LocalDateTime.now())
                }
                database.useConnection { connection ->
                    connection.prepareStatement("select last_insert_id()").use { resource ->
                        resource.executeQuery().asIterable().forEach { resultSet ->
                            articleId = resultSet.getInt(1)
                        }
                    }
                }
                database.batchInsert(ArticleToCategoryDAO) {
                    articleUploadVO.categories.forEach { id ->
                        item {
                            set(ArticleToCategoryDAO.category, id)
                            set(ArticleToCategoryDAO.article, articleId)
                        }
                    }
                }
                transaction.commit()
            }
        }
        val execute = withTimeoutOrNull(5000) {
            ossJob.join()
            sqlJob.join()
            "Done"
        }
        if (execute == "Done" && !ossJob.isCancelled && !sqlJob.isCancelled) {
            val query = getDefaultQuery().where {
                ArticleDAO.id eq articleId
            }
            val result: ArticleDTO? = getResult(query).getOrNull(0)
            if (result == null) {
                throw InternalError("插入失败")
            } else {
				return@coroutineScope result
            }
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

    override suspend fun getArticle(id: Int): ArticleDetailDTO {
        val res = getResult(getDefaultQuery().where { ArticleDAO.id eq id }).getOrNull(0)
            ?: throw NotFoundException()
        val item = ArticleDetailDTO(
            id = res.id,
            title = res.title,
            brief = res.brief,
            createAt = res.createAt,
            views = res.views,
            categories = res.categories,
            content = null
        )
        try {
            val ossObject = OSSInstance.getObject(OSSInfo.MARKDOWN.bucket, item.title + ".md")
            item.content = String(ossObject.objectContent.readBytes())
            return item
        } catch (e: OSSException) {
            throw NotFoundException(e.message)
        }
    }

    override suspend fun getAllArticleList(): List<ArticleAllDTO> {
        return database.from(ArticleDAO).select(ArticleDAO.id, ArticleDAO.title).map {
            ArticleAllDTO(it[ArticleDAO.id], it[ArticleDAO.title])
        }
    }
}
