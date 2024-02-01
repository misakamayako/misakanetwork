package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface Album : Entity<Album> {
	val id: Int
	val title: String
	val cover: String?
	val nsfw: Boolean
	val private: Boolean
}

object AlbumDAO : Table<Album>("album") {
	val id = int("id").primaryKey().bindTo { it.id }
	val title = varchar("title").bindTo { it.title }
	val cover = varchar("cover").bindTo { it.cover }
	val nsfw = boolean("nsfw").bindTo { it.nsfw }
	val private = boolean("private").bindTo { it.private }
}

interface Images : Entity<Images> {
	val id: Int
	val eigenvalues: String
	val name: String
	val nsfw: Boolean
	val private: Boolean
	val album: Album?
}

object ImagesDAO : Table<Images>("img") {
	val id = int("id").primaryKey().bindTo { it.id }
	val eigenvalues = varchar("eigenvalues").bindTo { it.eigenvalues }
	val name = varchar("name").bindTo { it.name }
	val nsfw = boolean("nsfw").bindTo { it.nsfw }
	val private = boolean("private").bindTo { it.private }
	val album = int("album").references(AlbumDAO) { it.album }
}

interface ImgToCategory : Entity<ImgToCategory> {
	val imgId: Images
	val category: Category
}

object ImgToCategoryDAO : Table<ImgToCategory>("img_to_category") {
	val imgId = int("img_id").references(ImagesDAO) { it.imgId }
	val category = int("category").references(CategoryDAO) { it.category }
}
