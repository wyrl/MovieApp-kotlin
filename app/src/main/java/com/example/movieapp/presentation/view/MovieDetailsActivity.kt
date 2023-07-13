package com.example.movieapp.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.ActivityMovieDetailsBinding

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        val i = intent
        val movie = i.getSerializableExtra("movie") as Movie
        binding.movie = movie
        loadImages(movie.imageUrl, movie.backdropImageUrl)
    }

    private fun loadImages(imageUrl: String?, backdropImageUrl: String?) {
        Glide.with(this)
            .load(imageUrl)
            .into(binding!!.includedLayout.imageView)
        Log.d(TAG, "Background Image Url: $backdropImageUrl")
        Glide.with(this)
            .load(backdropImageUrl)
            .into(binding!!.includedLayout.backgroundImage)
    }

    fun onBackClicked() {
        finish()
    }

    companion object {
        private val TAG = MovieDetailsActivity::class.java.simpleName
    }
}