package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface UserInfo: Entity<UserInfo> {
    val id:Int
    val name:String
    val privateKey:String
    val password:String
    val sessionId :String
}
object UserDao:Table<UserInfo>("user"){
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val privateKey = varchar("privateKey").bindTo { it.privateKey }
    val password = varchar("password").bindTo { it.password }
    val sessionId = varchar("sessionId").bindTo { it.sessionId }
}
