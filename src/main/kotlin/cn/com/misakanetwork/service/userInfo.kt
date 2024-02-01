package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.UserDao
import cn.com.misakanetwork.dto.LoginDTO
import cn.com.misakanetwork.dto.UserDto
import cn.com.misakanetwork.plugins.AuthenticationException
import cn.com.misakanetwork.plugins.AuthorizationException
import cn.com.misakanetwork.plugins.NotAcceptableException
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.tools.PasswordEncryption.authenticate
import cn.com.misakanetwork.tools.PasswordEncryption.generateSalt
import cn.com.misakanetwork.tools.PasswordEncryption.getEncryptedPassword
import org.ktorm.dsl.*


class UserService {
	fun getUserInfo(userSession: String): UserDto? {
		return database.from(UserDao).select().where {
			UserDao.sessionId eq userSession
		}.map {
			UserDto(
				id = it[UserDao.id],
				name = it[UserDao.name],
				password = it[UserDao.password],
				privateKey = it[UserDao.privateKey]
			)
		}.getOrNull(0)
	}

	fun login(loginDTO: LoginDTO): String {
		val foundUser = database.from(UserDao).select().where { UserDao.name eq loginDTO.name }.map {
			UserDto(
				id = it[UserDao.id],
				name = it[UserDao.name],
				password = it[UserDao.password],
				privateKey = it[UserDao.privateKey]
			)
		}.getOrNull(0) ?: throw AuthorizationException("The username and/or password you specified are/is not correct.")
		if (authenticate(loginDTO.password, foundUser.password!!, foundUser.privateKey ?: "")) {
			val newPrivateKey = generateSalt()
			val newSession = getEncryptedPassword(System.currentTimeMillis().toString(), newPrivateKey)
			database.update(UserDao) {
				set(UserDao.sessionId, newSession)
				where {
					UserDao.id eq foundUser.id!!
				}
			}
			return newSession
		} else {
			throw AuthorizationException("The username and/or password you specified are/is not correct.")
		}
	}

	fun put(loginDTO: LoginDTO): String {
		val count = database.from(UserDao).select(UserDao.name).where { UserDao.name eq loginDTO.name }.totalRecords
		if (count != 0) {
			throw NotAcceptableException("该用户已注册")
		}
		val newPrivateKey = generateSalt()
		val password = getEncryptedPassword(loginDTO.password, newPrivateKey)
		database.insert(UserDao) {
			set(UserDao.name, loginDTO.name)
			set(UserDao.privateKey, newPrivateKey)
			set(UserDao.password, password)
		}
		return "ok"
	}
}
