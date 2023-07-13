package com.example.movieapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movieapp.data.model.Movie

@Dao
interface MovieDao {
    @get:Query("SELECT * FROM movie")
    val all: List<Movie>

    @Insert
    fun insertAll(movies: List<Movie>)

    @Insert
    fun insert(movie: Movie)
}