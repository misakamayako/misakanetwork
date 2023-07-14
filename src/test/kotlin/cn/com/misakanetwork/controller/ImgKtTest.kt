package cn.com.misakanetwork.controller

import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ImgKtTest {

    @Test
    fun testGetImages() = testApplication {
        client.get("/images?page=1&pageSize=20").apply {
            assertEquals(status, HttpStatusCode.OK, "网络请求失败")
        }
    }
}
