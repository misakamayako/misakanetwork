package cn.com.misakanetwork.dto

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class UserDto(
	val id: Int?,
	val name: String?,
	@Transient
	val password:String?=null,
	@Transient
	val privateKey:String?=null
)

@Serializable
data class LoginDTO(
	@Required
	val name: String,
	@Required
	val password: String
)
