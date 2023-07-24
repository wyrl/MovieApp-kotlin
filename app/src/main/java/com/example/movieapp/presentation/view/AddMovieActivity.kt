package com.example.movieapp.presentation.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.utils.EncryptedDataStore
import com.example.movieapp.databinding.ActivityAddMovieBinding
import com.example.movieapp.presentation.viewmodel.AddMovieViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class AddMovieActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddMovieBinding
    private lateinit var viewModel: AddMovieViewModel
    private var movie: Movie? = null
    private lateinit var dataStore: EncryptedDataStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_movie)
        movie = Movie()
        binding.movie = movie
        binding.btnAddMovie.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnPickDate.setOnClickListener(this)
        viewModel = ViewModelProvider(this)[AddMovieViewModel::class.java]

        dataStore = EncryptedDataStore.getInstance(applicationContext)

        dataStore.get("authorization", "")?.let { viewModel.setAuthorization(it) }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnBack) {
            onBackClick()
            return
        }
        if (view.id == R.id.btnAddMovie) {
            onAddMovieClick()
            return
        }
        if (view.id == R.id.btnPickDate) {
            onPickDate()
            return
        }
    }

    private fun onBackClick() {
        finish()
    }

    private fun onAddMovieClick() {
        Log.d(TAG, "Movie: " + movie.toString())
        if (checkValid()) {
            showProgress(true)
            lifecycleScope.launch {
                try {
                    /*movie?.movieInfo?.let {
                        viewModel.addMovie(it)
                    }*/
                    movie?.let {
                        viewModel.addEncryptedMovie(it.movieInfo)
                    }
                    finish()
                } catch (ex: IOException) {
                    setErrorMessage("Network Failed!")
                    ex.message?.let { Log.e(TAG, it) }
                } finally {
                    showProgress(false)
                }
            }

        }
    }

    private fun showProgress(show: Boolean) {
        binding.btnAddMovie.visibility = if (!show) View.VISIBLE else View.GONE
        binding.loadingLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun checkValid(): Boolean {
        binding.txtErrMsg.text = ""
        if (isEmpty(movie?.title)) {
            setErrorMessage(getString(R.string.error_msg_empty, "title"))
            return false
        }
        if (isEmpty(movie?.dateReleased)) {
            setErrorMessage(getString(R.string.error_msg_empty, "year released"))
            return false
        }
        if (isEmpty(movie?.ratings)) {
            setErrorMessage(getString(R.string.error_msg_empty, "ratings"))
            return false
        }
        if (isEmpty(movie?.backdropImageUrl)) {
            setErrorMessage(getString(R.string.error_msg_empty, "image link"))
            return false
        }
        if (isEmpty(movie?.description)) {
            setErrorMessage(getString(R.string.error_msg_empty, "description"))
            return false
        }
        return true
    }

    private fun isEmpty(txt: Any?): Boolean {
        return txt == null || (txt as String).isEmpty()
    }

    private fun setErrorMessage(errorMessage: String) {
        binding.txtErrMsg.visibility = View.VISIBLE
        binding.txtErrMsg.text = errorMessage
    }

    private fun onPickDate() {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, y: Int, m: Int, d: Int ->
                val format = String.format("%d-%d-%d", y, m + 1, d)
                Log.d(TAG, "Date format: $format")
                val parser = SimpleDateFormat("yyyy-MM-dd")
                val convert = SimpleDateFormat("MMMM dd, YYYY")
                try {
                    val date = parser.parse(format)
                    val fullDate = convert.format(date)
                    movie!!.dateReleased = fullDate
                    Log.d(TAG, "Full Date: " + movie!!.dateReleased)
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    companion object {
        const val TAG = "AddMovieActivity"
    }
}