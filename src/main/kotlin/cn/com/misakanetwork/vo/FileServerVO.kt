package cn.com.misakanetwork.vo

import cn.com.misakanetwork.dto.FileMappingDTO

interface FileServerVO {
	suspend fun fileUpload(fileBytes: ByteArray, tail: String?): String
	suspend fun fileUploadSecurity(fileBytes: ByteArray): FileMappingDTO
}
