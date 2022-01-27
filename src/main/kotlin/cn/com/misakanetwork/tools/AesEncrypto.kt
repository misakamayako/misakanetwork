package cn.com.misakanetwork.tools

import cn.com.misakanetwork.CryptoConfig
import java.nio.charset.StandardCharsets
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object AesEncrypto {
    /* Private variable declaration */
    const val SECRET_KEY = CryptoConfig.SECRET_KEY
    const val SALTVALUE = CryptoConfig.SALT

    /* Encryption Method */
    fun encrypt(strToEncrypt: String): String? {
        try {
            /* Declare a byte array. */
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
            /* Return encrypted value. */
            return Base64.getEncoder()
                .encodeToString(cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8)))
        } catch (e: Exception) {
            println("Error occured during encryption: $e")
        }
        return null
    }

    private val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private val ivspec = IvParameterSpec(iv)

    /* Create factory for secret keys. */
    private val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")

    /* PBEKeySpec class implements KeySpec interface. */
    val spec: KeySpec = PBEKeySpec(
        SECRET_KEY.toCharArray(), SALTVALUE.toByteArray(), 65536, 256)
    val tmp = factory.generateSecret(spec)
    val secretKey = SecretKeySpec(tmp.encoded, "AES")

    /* Decryption Method */
    fun decrypt(strToDecrypt: String?): String {
        /* Declare a byte array. */

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec)
        /* Retruns decrypted value. */
        return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))
    }
}
