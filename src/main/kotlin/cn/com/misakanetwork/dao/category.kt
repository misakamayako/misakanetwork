package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface Category : Entity<Category> {
	val id: Int
	val description: String
	val type: Int
}

object CategoryDAO : Table<Category>("category") {
	val id = int("id").primaryKey().bindTo { it.id }
	val type = int("type").bindTo { it.type }
	var description = varchar("description").bindTo { it.description }
}
