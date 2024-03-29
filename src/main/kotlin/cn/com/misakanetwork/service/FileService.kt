package cn.com.misakanetwork.service

import cn.com.misakanetwork.dto.FileMappingDTO
import cn.com.misakanetwork.enum.OSSInfo
import cn.com.misakanetwork.plugins.AliOSS
import cn.com.misakanetwork.plugins.OSSInstance
import cn.com.misakanetwork.tools.calculateEigenvalues
import cn.com.misakanetwork.vo.FileServerVO
import com.aliyun.oss.ClientException
import com.aliyun.oss.OSSException
import java.io.ByteArrayInputStream

class FileService : FileServerVO {

	@Throws(OSSException::class, ClientException::class, Exception::class)
	override suspend fun fileUpload(fileBytes: ByteArray,ossInfo:OSSInfo, tail: String?): String {
		val eigen = calculateEigenvalues(fileBytes) + if (tail != null) ".$tail" else ""
		OSSInstance.putObject(
			ossInfo.bucket,
			eigen,
			ByteArrayInputStream(fileBytes)
		)
		return "https://${ossInfo.bucket}.${AliOSS.endpoint}/$eigen"
	}

	override suspend fun fileUploadSecurity(fileBytes: ByteArray): FileMappingDTO {
		TODO("Not yet implemented")
	}


//    override suspend fun fileUpload(belong: OSSInfo, fileBytes: ByteArray, realName: String?): FileMappingDTO? {
//        val eigenvalues: String = calculateEigenvalues(fileBytes)
//        val temp = database.from(FileMappingDAO).select().where { FileMappingDAO.eigenvalues eq eigenvalues }
//        if (temp.totalRecords == 0) {
//            val sqlJob = GlobalScope.launch {
//                database.insert(FileMappingDAO) {
//                    set(FileMappingDAO.eigenvalues, eigenvalues)
//                    set(FileMappingDAO.realName, realName)
//                    set(FileMappingDAO.createAt, LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
//                }
//            }
//            val ossJob = GlobalScope.launch {
//                OSSInstance.putObject(belong.bucket, eigenvalues, ByteArrayInputStream(fileBytes))
//            }
//            withTimeoutOrNull(5000) {
//                sqlJob.join()
//                ossJob.join()
//            }
//        }
//        var result: FileMappingDTO? = null
//        database.from(FileMappingDAO).select().where { FileMappingDAO.eigenvalues eq eigenvalues }.map {
//            result = FileMappingDTO(
//                it[FileMappingDAO.id]!!,
//                it[FileMappingDAO.eigenvalues]!!,
//                it[FileMappingDAO.realName],
//                it[FileMappingDAO.createAt]!!,
//                it[FileMappingDAO.deleteFlag]!!,
//            )
//        }
//        return if (result?.deleteFlag != false) {
//            null
//        } else {
//            result
//        }
//    }
}
