package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface Article: Entity<Article> {
    val id:Int
    val title: String
//    val saveLocation:String
    val label:String?
}

object ArticleDAO: Table<Article>("article"){
    val id = int("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
//    val saveLocation = varchar("saveLocation").bindTo { it.saveLocation }
    val label = varchar("label").bindTo { it.label }
}
