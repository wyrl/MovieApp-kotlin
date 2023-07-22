package com.example.movieapp.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.BR
import java.io.Serializable

@Entity
open class Movie : BaseObservable, Serializable {

    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "title")
    @Bindable
    private var _title: String? = null
    var title: String
        get() = _title ?: ""
        set(value) {
            _title = value
            notifyPropertyChanged(BR.title)
        }

    @ColumnInfo(name = "description")
    @Bindable
    private var _description: String? = null
    var description: String
        get() = _description ?: ""
        set(value) {
            _description = value
            notifyPropertyChanged(BR.description)
        }

    @ColumnInfo(name = "year_released")
    @Bindable
    private var _dateReleased: String? = null
    var dateReleased: String
        get() = _dateReleased ?: ""
        set(value) {
            _dateReleased = value
            notifyPropertyChanged(BR.dateReleased)
        }

    @ColumnInfo(name = "ratings")
    @Bindable
    private var _ratings: String? = null
    var ratings: String
        get() = _ratings ?: ""
        set(value) {
            _ratings = value
            notifyPropertyChanged(BR.ratings)
        }

    @ColumnInfo(name = "image_url")
    @Bindable
    private var _imageUrl: String? = null
    var imageUrl: String
        get() = _imageUrl ?: ""
        set(value) {
            _imageUrl = value
            notifyPropertyChanged(BR.imageUrl)
        }

    @ColumnInfo(name = "backdrop_image_url")
    @Bindable
    private var _backdropImageUrl: String? = null
    var backdropImageUrl: String
        get() = _backdropImageUrl ?: ""
        set(value) {
            _backdropImageUrl = value
            notifyPropertyChanged(BR.backdropImageUrl)
        }

    constructor()
    constructor(
        title: String,
        description: String,
        dateReleased: String,
        ratings: String,
        imageUrl: String,
        backdropImageUrl: String
    ) {
        this._title = title
        this.description = description
        this.dateReleased = dateReleased
        this.ratings = ratings
        this.imageUrl = imageUrl
        this.backdropImageUrl = backdropImageUrl
    }

    val movieInfo: MovieInfo
        get() = MovieInfo(
            title, dateReleased, description, ratings, listOf(imageUrl, backdropImageUrl)
        )

    override fun toString(): String {
        return "Movie Title: $title, Date Released: $dateReleased"
    }

    companion object {
        fun convertFrom(movieInfoList: List<MovieInfo>): List<Movie> {
            val movies: MutableList<Movie> = ArrayList()
            for (info in movieInfoList) {
                movies.add(
                    Movie(
                        info.title,
                        info.plot,
                        info.released,
                        info.imdbRating,
                        info.images[0],
                        info.images[1]
                    )
                )
            }

            return movies
        }
    }
}