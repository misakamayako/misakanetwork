package cn.com.misakanetwork.tools

import com.auth0.jwt.algorithms.Algorithm
import org.bouncycastle.crypto.Signer
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.Security
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


//object Encryption {
//    fun encodePassword(s:String):String{
//        Security.addProvider(BouncyCastleProvider())
//        val cipher = Cipher.getInstance(Algorithm.RSA256.getType())//"AES/ECB/PKCS7Padding")
//        cipher.init(Cipher.ENCRYPT_MODE,
//            SecretKeySpec("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".toByteArray(StandardCharsets.UTF_8), "AES"))
//        val d = cipher.doFinal(s.toByteArray(StandardCharsets.UTF_8))
////        cipher.getOutputSize()
//
//        return Base64.getEncoder().encodeToString(d);
//    }
//}
//
