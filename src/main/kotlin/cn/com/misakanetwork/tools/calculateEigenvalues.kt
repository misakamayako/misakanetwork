package cn.com.misakanetwork.tools

import java.security.MessageDigest

fun calculateEigenvalues(fileBytes: ByteArray): String {
	val messageDigest = MessageDigest.getInstance("sha-256")
	messageDigest.update(fileBytes)
	val digest = messageDigest.digest()
	val result = StringBuilder()
	digest.forEach {
		result.append(Integer.toHexString(it.toInt() and 0xFF))
	}
	return String(result)
}
