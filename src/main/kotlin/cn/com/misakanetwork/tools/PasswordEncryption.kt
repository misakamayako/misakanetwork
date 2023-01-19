package cn.com.misakanetwork.tools

import java.math.BigInteger
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordEncryption {
	const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1"

	/**
	 * 盐的长度
	 */
	const val SALT_BYTE_SIZE = 32 / 2

	/**
	 * 生成密文的长度
	 */
	const val HASH_BIT_SIZE = 128 * 4

	/**
	 * 迭代次数
	 */
	const val PBKDF2_ITERATIONS = 1000

	/**
	 * 对输入的password进行验证
	 *
	 * @param attemptedPassword
	 * 待验证的password
	 * @param encryptedPassword
	 * 密文
	 * @param salt
	 * 盐值
	 * @return 是否验证成功
	 */
	@Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
	fun authenticate(attemptedPassword: String, encryptedPassword: String, salt: String): Boolean {
		// 用同样的盐值对用户输入的password进行加密
		val encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt)
		// 把加密后的密文和原密文进行比較，同样则验证成功。否则失败
		return encryptedAttemptedPassword == encryptedPassword
	}

	/**
	 * 生成密文
	 *
	 * @param password
	 * 明文password
	 * @param salt
	 * 盐值
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	@Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
	fun getEncryptedPassword(password: String, salt: String): String {
		val spec: KeySpec = PBEKeySpec(password.toCharArray(), fromHex(salt), PBKDF2_ITERATIONS, HASH_BIT_SIZE)
		val f = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
		return toHex(f.generateSecret(spec).encoded)
	}

	/**
	 * 通过提供加密的强随机数生成器 生成盐
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@Throws(NoSuchAlgorithmException::class)
	fun generateSalt(): String {
		val random = SecureRandom.getInstance("SHA1PRNG")
		val salt = ByteArray(SALT_BYTE_SIZE)
		random.nextBytes(salt)
		return toHex(salt)
	}

	/**
	 * 十六进制字符串转二进制字符串
	 *
	 * @param   hex         the hex string
	 * @return              the hex string decoded into a byte array
	 */
	private fun fromHex(hex: String): ByteArray {
		val binary = ByteArray(hex.length / 2)
		for (i in binary.indices) {
			binary[i] = hex.substring(2 * i, 2 * i + 2).toInt(16).toByte()
		}
		return binary
	}

	/**
	 * 二进制字符串转十六进制字符串
	 *
	 * @param   array       the byte array to convert
	 * @return              a length*2 character string encoding the byte array
	 */
	private fun toHex(array: ByteArray): String {
		val bi = BigInteger(1, array)
		val hex = bi.toString(16)
		val paddingLength = array.size * 2 - hex.length
		return if (paddingLength > 0) String.format("%0" + paddingLength + "d", 0) + hex else hex
	}
}
