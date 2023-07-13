package com.example.movieapp.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieInfo(
    @field:Expose
    @field:SerializedName("Title")
    var title: String,
    @field:Expose @field:SerializedName(
        "Released"
    ) val released: String,
    @field:Expose @field:SerializedName("Plot") val plot: String,
    @field:Expose @field:SerializedName(
        "imdbRating"
    ) val imdbRating: String,
    @field:Expose @field:SerializedName("Images") val images: List<String>
)