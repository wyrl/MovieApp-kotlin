package com.example.movieapp.data.utils

import com.example.movieapp.data.model.Movie

class EncryptedMovie(private val key: String, private val movie: Movie) : Movie() {

    init {
        doEncrypt()
    }

    private fun doEncrypt(){
        val movie = this.movie
        title = CryptoUtil.encrypt(key, movie.title)
        description = CryptoUtil.encrypt(key, movie.description)
        dateReleased = CryptoUtil.encrypt(key, movie.dateReleased)
        ratings = CryptoUtil.encrypt(key, movie.ratings)
        imageUrl = CryptoUtil.encrypt(key, movie.imageUrl)
        backdropImageUrl = CryptoUtil.encrypt(key, movie.backdropImageUrl)
    }
}