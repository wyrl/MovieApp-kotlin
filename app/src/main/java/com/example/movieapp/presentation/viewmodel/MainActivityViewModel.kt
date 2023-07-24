package com.example.movieapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.data.utils.ApiResponse
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository
    private val _selectedMovie: MutableLiveData<Movie?>
    private val _apiResponse: MutableLiveData<ApiResponse>

    init {
        repository = MovieRepository(application)
        _selectedMovie = MutableLiveData()
        _apiResponse = MutableLiveData()
        viewModelScope.launch{
            try {
                repository.loadData()
                _apiResponse.postValue(ApiResponse(true, ""))
            }
            catch (ex: IOException){
                _apiResponse.postValue(ApiResponse(false, "Network Failed!"))
                Log.e("MainActivityViewModel", ex.message ?: "")
            }
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

    fun hasSelectedMovie(): Boolean{
        return _selectedMovie.value != null
    }

    fun getFirstMovieItem(): Movie? {
        if(!repository.movies.value.isNullOrEmpty()){
            return repository.movies.value!![0]
        }
        return null
    }

    fun apiResponse(): LiveData<ApiResponse>{
        return _apiResponse
    }

    suspend fun refreshListFromLocal() {
        repository.refreshListFromLocal()
    }
}