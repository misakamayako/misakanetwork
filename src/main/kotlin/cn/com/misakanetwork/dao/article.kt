package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDateTime

interface ArticleCategory : Entity<ArticleCategory> {
    val id: Int
    val description: String
}

interface Article : Entity<Article> {
    val id: Int
    val title: String
    val brief: String?
    val createAt: LocalDateTime
    val views:Int
}

interface ArticleToCategory : Entity<ArticleToCategory> {
    val article: Article
    val category: ArticleCategory
}

object ArticleCategoryDAO : Table<ArticleCategory>("article_category") {
    val id = int("id").primaryKey().bindTo { it.id }
    var description = varchar("description").bindTo { it.description }
}

object ArticleDAO : Table<Article>("article") {
    val id = int("id").primaryKey().bindTo { it.id }
    val views = int("views").bindTo { it.views }
    val title = varchar("title").bindTo { it.title }
    val brief = varchar("brief").bindTo { it.brief }
    val createAt = datetime("createAt").bindTo { it.createAt }
}

object ArticleToCategoryDAO : Table<ArticleToCategory>("article_to_category") {
    val article = int("article").references(ArticleDAO) { it.article }
    val category = int("category").references(ArticleCategoryDAO) { it.category }
}
