package com.example.movieapp.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.data.database.MovieDatabase
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieInfo
import com.example.movieapp.data.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MovieRepository(application: Application) {
    private val TAG = "MovieRepository"
    val movies: MutableLiveData<List<Movie>>
    private val db: MovieDatabase?
    private val context: Context



    init {
        context = application.applicationContext
        movies = MutableLiveData(ArrayList())
        db = MovieDatabase.getDatabase(context)
    }

    suspend fun loadData() = withContext(Dispatchers.IO) {
        Log.d(TAG, "loadData")
        val movieList = db?.movieDao()?.all
        if (!movieList.isNullOrEmpty()) {
            Log.d(TAG, "Load from database")
            movies.postValue(movieList)
        } else {
            Log.d(TAG, "Load from API")
            fetchFromAPI()
        }
    }

    suspend fun refreshListFromLocal() = withContext(Dispatchers.IO) {
        val movieList = db?.movieDao()?.all
        movies.postValue(movieList)
    }

    private fun fetchFromAPI() = runBlocking {
        Log.d(TAG, "fetchFromAPI")
        val response = RetrofitInstance.api.getMovies()

        if(response.isSuccessful){
            val movieList: List<Movie>? = response.body()?.let { Movie.convertFrom(it) }
            movieList?.let {
                saveIntoDatabase(it)
                movies.postValue(movieList)
            }

        } else {
            Log.e("MovieRepository", "onReponse --> failure")
        }
    }

    private fun saveIntoDatabase(movieList: List<Movie>) {
        Log.d(TAG, "saveIntoDatabase")
        if (movies.value == null || movies.value!!.isEmpty()) {
            Log.d(TAG, "Movie List Count: " + movieList.size)
            db!!.movieDao().insertAll(movieList)
        }
    }

    private fun insertMovie(movie: Movie) {
        db?.movieDao()?.insert(movie)
    }

    suspend fun addMovie(movieInfo: MovieInfo, authorization: String) : MovieInfo? {
        val result = RetrofitInstance.api.addMovie(movieInfo, authorization)

        if(result.isSuccessful){
            val info: MovieInfo? = result.body()
            if(info != null){
                insertMovie(
                    Movie(
                        info.title,
                        info.plot,
                        info.released,
                        info.imdbRating,
                        info.images[0],
                        info.images[0]
                    )
                )
            }
            return info
        }
        else{
            throw Exception(result.message())
        }

    }
}