package cn.com.misakanetwork.vo

import cn.com.misakanetwork.dto.FileMappingDTO
import cn.com.misakanetwork.enum.OSSInfo

interface FileServerVO {
	suspend fun fileUpload(fileBytes: ByteArray, ossInfo: OSSInfo, tail: String?): String
	suspend fun fileUploadSecurity(fileBytes: ByteArray): FileMappingDTO
}
