package com.example.movieapp.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.presentation.adapter.MoviesAdapter.MovieViewHolder

class MoviesAdapter(private val clickHandlers: ClickHandlers) : RecyclerView.Adapter<MovieViewHolder>() {
    private var movieList: List<Movie> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = DataBindingUtil.inflate<MovieItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.movie_item,
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList!![position]
        Log.d("MoviesAdapter", "Movie Title: " + movie.title)
        holder.binding.movie = movie
        holder.itemView.setOnClickListener { clickHandlers.onSelected(movie) }
        Glide.with(holder.binding.root)
            .load(movie.backdropImageUrl)
            .into(holder.binding.bgImg)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    fun setMovieList(movieList: List<Movie>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    interface ClickHandlers {
        fun onSelected(movie: Movie)
    }
}