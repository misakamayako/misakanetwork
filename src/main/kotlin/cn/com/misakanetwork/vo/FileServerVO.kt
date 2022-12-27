package cn.com.misakanetwork.vo

import cn.com.misakanetwork.dto.FileMappingDTO

interface FileServerVO {
    fun calculateEigenvalues(fileBytes: ByteArray): String
    suspend fun fileUpload(fileBytes: ByteArray,tail:String?): String
    suspend fun fileUploadSecurity(fileBytes: ByteArray): FileMappingDTO
}
