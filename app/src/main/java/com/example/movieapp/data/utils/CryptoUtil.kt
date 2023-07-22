package com.example.movieapp.data.utils

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class CryptoUtil {
    companion object{
        private const val AES_KEY_SIZE = 256

        fun encrypt(key: String, input: String): String {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, generateAESKey(key))
            val encryptedData = cipher.doFinal(input.toByteArray(Charsets.UTF_8))

            return Base64.encodeToString(encryptedData, Base64.DEFAULT)
        }

        fun decrypt(key: String, encryptedData: String): String{
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, generateAESKey(key))
            val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)
            val decryptedData = cipher.doFinal(decodedData)
            return decryptedData.toString(Charsets.UTF_8)
        }

        private fun generateAESKey(key: String): SecretKey {
            val keyBytes = key.toByteArray(Charsets.UTF_8)
            return SecretKeySpec(getValidKeyBytes(keyBytes), "AES")
        }

        private fun getValidKeyBytes(key: ByteArray): ByteArray {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            return messageDigest.digest(key).copyOf(AES_KEY_SIZE / 8)
        }
    }
}