package cn.com.misakanetwork.service.user

import cn.com.misakanetwork.dao.UserDao
import cn.com.misakanetwork.dto.LoginDTO
import cn.com.misakanetwork.dto.Response
import cn.com.misakanetwork.dto.UserDto
import cn.com.misakanetwork.plugins.AuthenticationException
import cn.com.misakanetwork.plugins.AuthorizationException
import cn.com.misakanetwork.plugins.UserSession
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.tools.PasswordEncryption.authenticate
import cn.com.misakanetwork.tools.PasswordEncryption.generateSalt
import cn.com.misakanetwork.tools.PasswordEncryption.getEncryptedPassword
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.sessions.*
import org.ktorm.dsl.*

class UserService(private val call: ApplicationCall) {
    suspend fun getMethod() {
        val userSession = call.sessions.get<UserSession>() ?: throw AuthenticationException()
        val result = database.from(UserDao).select().where {
            UserDao.sessionId eq userSession.sessionId
        }
        for (i in result) {
            call.respond(Response(data = UserDto(i[UserDao.id], i[UserDao.name])))
            return
        }
        throw AuthenticationException()
    }

    suspend fun post(loginDTO: LoginDTO) {
        val foundUser = database.from(UserDao).select().where { UserDao.name eq loginDTO.name }
        var userDto: UserDto? = null
        var passWord: String? = null
        var privateKey: String? = null
        for (i in foundUser) {
            userDto = UserDto(i[UserDao.id], i[UserDao.name])
            passWord = i[UserDao.password]
            privateKey = i[UserDao.privateKey]
            break
        }
        if (userDto == null || passWord == null) {
            throw AuthorizationException()
        }
        if (authenticate(loginDTO.password, passWord, privateKey ?: "")) {
            val newPrivateKey = generateSalt()
            val sessionId = getEncryptedPassword(System.currentTimeMillis().toString(), newPrivateKey)
            database.update(UserDao) {
                set(it.sessionId, sessionId)
                where {
                    it.id eq userDto.id!!
                }
            }
            call.sessions.set(UserSession(sessionId))
            call.respond(Response(data = userDto))
        } else {
            throw AuthorizationException()
        }
    }

    suspend fun put(loginDTO: LoginDTO) {
        val count = database.from(UserDao).select(UserDao.name).where { UserDao.name eq loginDTO.name }.totalRecords
        if (count != 0) {
            throw AuthorizationException("该用户已注册")
        }
        val newPrivateKey = generateSalt()
        val password = getEncryptedPassword(loginDTO.password, newPrivateKey)
        database.insert(UserDao) {
            set(UserDao.name, loginDTO.name)
            set(UserDao.privateKey, newPrivateKey)
            set(UserDao.password, password)
        }
        call.respond(Response(data = true))
    }

}