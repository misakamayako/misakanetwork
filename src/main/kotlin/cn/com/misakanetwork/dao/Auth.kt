package cn.com.misakanetwork.dao

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Auth : Table<Nothing>("Auth") {
	val id = int("id").primaryKey()
	val userName = varchar("userName")
	val password = varchar("password")
}

//fun main() {
//    for (row in SQLServer.database.from(Auth).select()){
//        println(row[Auth.id])
//    }
//}
