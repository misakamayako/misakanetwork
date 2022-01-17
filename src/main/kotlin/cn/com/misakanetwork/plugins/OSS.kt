package cn.com.misakanetwork.plugins

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import kotlin.reflect.KProperty


val OSSInstance by AliOSS

object AliOSS {
    private const val endpoint = "https://oss-cn-shanghai.aliyuncs.com"

    private const val accessKeyId = "LTAI5t7FLzZySw7ue8CQcJhZ"
    private const val accessKeySecret = "26kwt5y0RFMCqbGOTVsvEIocdjgll1"
    private val ossClient: OSS = OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret)
    operator fun getValue(nothing: Nothing?, property: KProperty<*>):OSS {
        return ossClient
    }
}
