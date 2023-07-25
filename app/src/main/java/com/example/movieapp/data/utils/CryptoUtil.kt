package com.example.movieapp.data.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.movieapp.BuildConfig
import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoUtil {
    companion object {
        private const val AES_KEY_SIZE = 256

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

        private const val keyString: String = BuildConfig.SECRET_KEY

        private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        private fun getKey(): SecretKey {
            val existingKey = keyStore.getEntry(keyString, null) as? KeyStore.SecretKeyEntry
            return existingKey?.secretKey ?: generateAESKey()
        }

        fun encrypt(input: String): String {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getKey())
            val encryptedData = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
            val iv = cipher.iv
            val combinedData = ByteArray(iv.size + encryptedData.size)
            System.arraycopy(iv, 0, combinedData, 0, iv.size)
            System.arraycopy(encryptedData, 0, combinedData, iv.size, encryptedData.size)
            return Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        }

        fun decrypt(encryptedData: String): String {
            val decodedData = Base64.decode(encryptedData, Base64.NO_WRAP)
            val iv = IvParameterSpec(decodedData.copyOfRange(0, 16))
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), iv)
            val decryptedData = cipher.doFinal(decodedData.copyOfRange(16, decodedData.size))
            return decryptedData.toString(Charsets.UTF_8)
        }

        private fun generateAESKey(): SecretKey {
            /*val keyBytes = keyString.toByteArray(Charsets.UTF_8)
            return SecretKeySpec(getValidKeyBytes(keyBytes), ALGORITHM)*/

            return KeyGenerator.getInstance(ALGORITHM).apply {
                init(
                    KeyGenParameterSpec.Builder(
                        keyString,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING)
                        .setUserAuthenticationRequired(false)
                        .setRandomizedEncryptionRequired(true)
                        .build()
                )
            }.generateKey()
        }

        /*private fun getValidKeyBytes(key: ByteArray): ByteArray {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            return messageDigest.digest(key).copyOf(AES_KEY_SIZE / 8)
        }*/
    }
}