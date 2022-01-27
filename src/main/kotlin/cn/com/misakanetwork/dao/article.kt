package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface ArticleCategory : Entity<ArticleCategory> {
    val id: Int
    val description: String
}

interface Article : Entity<Article> {
    val id: Int
    val title: String
    val category: Int
}

interface ArticleToCategory : Entity<ArticleToCategory> {
    val article: Article
    val category: ArticleCategory
}

object ArticleCategoryDAO : Table<ArticleCategory>("article_category") {
    val id = int("id").primaryKey().bindTo { it.id }
    val description = varchar("description").bindTo { it.description }
}

object ArticleDAO : Table<Article>("article") {
    val id = int("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val category = int("category").bindTo { it.category }
}

object CategoryToArticleDAO : Table<ArticleToCategory>("category_to_article") {
    val article = int("article").references(ArticleDAO) { it.article }
    val category = int("category").references(ArticleCategoryDAO) { it.category }
}
