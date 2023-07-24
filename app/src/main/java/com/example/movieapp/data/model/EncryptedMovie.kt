package com.example.movieapp.data.model

import com.example.movieapp.data.utils.CryptoUtil
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EncryptedMovie(
    @Transient
    private val movieInfo: MovieInfo
) {

    @Expose
    @SerializedName("encryptedMovie")
    private lateinit var _encryptedData: String

    init {
        doEncrypt()
    }

    private fun doEncrypt() {
        val gson = Gson()
        val jsonString = gson.toJson(movieInfo)

        _encryptedData = CryptoUtil.encrypt(jsonString)
    }

    fun getEncryptedData(): String {
        return _encryptedData
    }
}