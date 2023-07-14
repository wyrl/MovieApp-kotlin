package com.example.movieapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository
    private val _selectedMovie: MutableLiveData<Movie?>

    init {
        repository = MovieRepository(application)
        _selectedMovie = MutableLiveData()
        viewModelScope.launch{
            repository.loadData()
        }
    }

    fun getMovieList(): LiveData<List<Movie>> {
        return repository.movies
    }

    fun getSelectedMovie(): LiveData<Movie?> {
        return _selectedMovie
    }

    fun updateSelectedMovie(movie: Movie?) {
        _selectedMovie.value = movie
    }

    suspend fun refreshListFromLocal() {
        repository.refreshListFromLocal()
    }
}