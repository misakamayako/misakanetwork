package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.tools.AesEncrypto
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import kotlin.reflect.KProperty


val OSSInstance by AliOSS

object AliOSS {
    private const val endpoint = "https://oss-cn-shanghai.aliyuncs.com"

    @JvmStatic
    val accessKeyId = AesEncrypto.decrypt("oyKdtKxzvaouj+AX+WgdjpQ7fri0jkSdmHHEuNprjPE=")

    @JvmField
    val accessKeySecret = AesEncrypto.decrypt("8lbiKCMuPMg1r66Ny/7Ie1S71nZh3U4CiyIbJliW+GM=")

    private val ossClient: OSS = OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret)

    operator fun getValue(nothing: Nothing?, property: KProperty<*>) = ossClient
}
