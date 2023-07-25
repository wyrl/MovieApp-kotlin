package com.example.movieapp.data.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.movieapp.BuildConfig
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoUtil {
    companion object {
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
            return Base64.encodeToString(
                encryptedData,
                Base64.NO_WRAP
            ) + "\n" + Base64.encodeToString(iv, Base64.NO_WRAP)
        }

        fun decrypt(encryptedData: String): String {
            val base64Arr = encryptedData.split("\n")
            val (ivBase64, cipherBase64) = base64Arr

            val iv = IvParameterSpec(Base64.decode(ivBase64, Base64.NO_WRAP))
            val cipherData = Base64.decode(cipherBase64, Base64.NO_WRAP)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), iv)
            val decryptedData = cipher.doFinal(cipherData)


            return decryptedData.toString(Charsets.UTF_8)
        }

        private fun generateAESKey(): SecretKey {
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
    }
}