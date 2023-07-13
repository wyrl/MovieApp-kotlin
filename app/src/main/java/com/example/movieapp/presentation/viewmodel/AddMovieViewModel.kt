package com.example.movieapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.movieapp.data.model.MovieInfo
import com.example.movieapp.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    private var authorization: String? = null

    init {
        repository = MovieRepository(application)
    }

    suspend fun addMovie(movieInfo: MovieInfo) = withContext(Dispatchers.IO){
        repository.addMovie(movieInfo, authorization ?: "")
    }

    fun setAuthorization(authorization: String){
        this.authorization = authorization
        Log.d("AddMovieViewModel", "Authorization: $authorization")
    }
}