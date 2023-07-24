package com.example.movieapp.data.model

import com.example.movieapp.data.utils.CryptoUtil
import com.google.gson.Gson

class DecryptedMovie(private val encryptedData: String){
    private lateinit var _decryptedMovieInfo: MovieInfo

    init {
        doDecrypt()
    }

    private fun doDecrypt() {
        val jsonString = CryptoUtil.decrypt(encryptedData)
        val gson = Gson()
        _decryptedMovieInfo = gson.fromJson(jsonString, MovieInfo::class.java)
    }

    fun getDecryptedMovieInfo(): MovieInfo{
        return _decryptedMovieInfo
    }
}
