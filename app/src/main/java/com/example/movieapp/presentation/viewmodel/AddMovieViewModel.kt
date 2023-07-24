package com.example.movieapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.movieapp.data.model.EncryptedMovie
import com.example.movieapp.data.model.MovieInfo
import com.example.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    private var authorization: String = ""

    init {
        repository = MovieRepository(application)
    }

    suspend fun addMovie(movieInfo: MovieInfo) = withContext(Dispatchers.IO) {
        repository.addMovie(movieInfo, authorization)
    }

    suspend fun addEncryptedMovie(movieInfo: MovieInfo) = withContext(Dispatchers.IO) {
        val encryptedMovie = EncryptedMovie(movieInfo)
        repository.addEncryptedMovie(encryptedMovie, authorization)
    }

    fun setAuthorization(authorization: String) {
        this.authorization = authorization
        Log.d("AddMovieViewModel", "Authorization: $authorization")
    }
}