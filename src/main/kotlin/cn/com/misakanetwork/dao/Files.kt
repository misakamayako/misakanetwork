package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

interface FileMapping : Entity<FileMapping> {
	val id: Int
	val eigenvalues: String
	val realName: String?
	val createAt: LocalDateTime
	val deleteFlag: Boolean
}

object FileMappingDAO : Table<FileMapping>("file_mapping") {
	val id = int("id").primaryKey().bindTo { it.id }
	val eigenvalues = varchar("eigenvalues").bindTo { it.eigenvalues }
	val realName = varchar("real_name").bindTo { it.realName }
	val createAt = datetime("create_at").bindTo { it.createAt }
	val deleteFlag = boolean("delete_flag").bindTo { it.deleteFlag }
}
