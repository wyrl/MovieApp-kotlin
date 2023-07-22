package com.example.movieapp.data.utils

import com.example.movieapp.data.model.Movie

class DecryptedMovie(private val key: String, private val movie: Movie) : Movie() {


    init {
        doEncrypt()
    }

    private fun doEncrypt() {
        val movie = this.movie
        title = CryptoUtil.decrypt(key, movie.title)
        description = CryptoUtil.decrypt(key, movie.description)
        dateReleased = CryptoUtil.decrypt(key, movie.dateReleased)
        ratings = CryptoUtil.decrypt(key, movie.ratings)
        imageUrl = CryptoUtil.decrypt(key, movie.imageUrl)
        backdropImageUrl = CryptoUtil.decrypt(key, movie.backdropImageUrl)
    }
}
