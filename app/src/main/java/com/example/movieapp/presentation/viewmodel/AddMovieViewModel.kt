package com.example.movieapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.movieapp.data.model.MovieInfo
import com.example.movieapp.repository.MovieRepository

class AddMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    init {
        repository = MovieRepository(application)
    }

    suspend fun addMovie(movieInfo: MovieInfo){
        repository.addMovie(movieInfo)
    }
}