package com.example.movieapp.service

import com.example.movieapp.data.model.MovieInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MovieAPI {
    @GET("/movies")
    suspend fun getMovies(): Response<List<MovieInfo>>

    @Headers("Authorization: dGVzdDpzZWNyZXQ=")
    @POST("/add-movie")
    suspend fun addMovie(@Body movieInfo: MovieInfo): Response<MovieInfo>
}