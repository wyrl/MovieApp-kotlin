package com.example.movieapp.presentation.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.utils.EncryptedDataStore
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.presentation.adapter.MoviesAdapter
import com.example.movieapp.presentation.adapter.MoviesAdapter.ClickHandlers
import com.example.movieapp.presentation.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ClickHandlers, View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: MoviesAdapter

    private lateinit var dataStore: EncryptedDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]



        with(binding) {
            viewModel = viewModel
            lifecycleOwner = this@MainActivity
            appBarLayout.btnAdd.setOnClickListener(this@MainActivity)
        }

        setupRecyclerAdapter()

        observer()

        dataStore = EncryptedDataStore.getInstance(applicationContext)
        dataStore.put("authorization", "dGVzdDpzZWNyZXQ=")
    }

    private fun setupRecyclerAdapter() {
        adapter = MoviesAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.getMovieList().observe(this) { movies ->
            Log.d(TAG, "SetupRecyclerAdapter observer -> movie list count: " + movies.size)
            setDefaultIfNotSelectedItem()
            adapter.setMovieList(movies)
        }
    }

    private fun observer() {
        viewModel.apiResponse().observe(this) { apiResponse ->
            if (!apiResponse.isSuccess) {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Network Failed!")
                alertDialogBuilder.setMessage("Oops! It seems the local server is not running yet.")

                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finishAffinity()
                }

                alertDialogBuilder.create().show()
            }
        }
    }

    private fun setDefaultIfNotSelectedItem() {
        if (!viewModel.hasSelectedMovie()) { // Default Selected first item
            viewModel.updateSelectedMovie(viewModel.getFirstMovieItem())
        }
    }

    override fun onSelected(movie: Movie) {
        val resources = resources
        val config = resources.configuration
        Log.i(TAG, "Orientation: " + config.orientation)
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("movie", movie)
            startActivity(intent)
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewModel.updateSelectedMovie(movie)
            loadMovieDetailsImage(movie.backdropImageUrl)
        }
    }

    private fun loadMovieDetailsImage(url: String) {
        Glide.with(this)
            .load(url)
            .into(binding.includedLayout!!.imageView)
    }

    override fun onClick(v: View) { // On Click Add button
        val intent = Intent(this, AddMovieActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refreshListFromLocal()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}