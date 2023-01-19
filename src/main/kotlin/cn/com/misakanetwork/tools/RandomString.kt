package cn.com.misakanetwork.tools

import kotlin.random.Random

object RandomString {
	const val string = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
	fun getString(length: Int): String {
		val sb = StringBuilder()
		for (i in 0 until length) {
			sb.append(string[Random.nextInt(0, 62)])
		}
		return String(sb)
	}
}
