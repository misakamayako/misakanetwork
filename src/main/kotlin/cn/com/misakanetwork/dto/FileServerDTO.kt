package cn.com.misakanetwork.dto

import cn.com.misakanetwork.tools.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDateTime

@Serializable
data class FileMappingDTO(
    val id: Int,
    val eigenvalues: String,
    val realName: String?,
    @Serializable(with = LocalDateSerializer::class)
    val createAt: LocalDateTime,
    @Transient
    val deleteFlag: Boolean = true,
)
