package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface Album : Entity<Album> {
	val id: Int
	val title: String
	val nsfw: Boolean
	val private: Boolean
}

object AlbumDAO : Table<Album>("album") {
	val id = int("id").primaryKey().bindTo { it.id }
	val title = varchar("title").bindTo { it.title }
	val nsfw = boolean("nsfw").bindTo { it.nsfw }
	val private = boolean("private").bindTo { it.private }
}
