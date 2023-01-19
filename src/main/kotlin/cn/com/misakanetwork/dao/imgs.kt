package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.*
interface Album:Entity<Album>{
	val id:Int
	val title:String
	val nsfw:Int
	val private:Boolean
}
object AlbumDAO:Table<Album>("album"){
	val id = int("id").primaryKey().bindTo { it.id }
	val title = varchar("title").bindTo { it.title }
	val nsfw = int("nsfw").bindTo { it.nsfw }
	val private = boolean("private").bindTo { it.private }
}
