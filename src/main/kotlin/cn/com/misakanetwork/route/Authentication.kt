package cn.com.misakanetwork.route

import cn.com.misakanetwork.dao.Auth
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.tools.PasswordEncryption
import io.ktor.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.ktorm.dsl.*



fun Application.user() {
    GlobalScope.launch {
        val result =
            database.from(Auth).select().where { Auth.userName eq "misaka" }.limit(offset = 0, limit = 1)
        if (result.totalRecords == 0) {
            database.insert(Auth) {
                set(it.id, 0)
                set(it.userName, "misaka")
                set(it.password, PasswordEncryption.getEncryptedPassword("125125YZd","password"))
            }
        }
    }
}
