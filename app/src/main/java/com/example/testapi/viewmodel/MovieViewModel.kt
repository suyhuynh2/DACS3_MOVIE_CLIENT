package com.example.testapi.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.pusher.MoviePusherService
import com.example.testapi.data.mode_data.Movie
import com.example.testapi.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val movieRepository = MovieRepository()

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var isInitialLoad = true
    private var moviePusherService: MoviePusherService? = null



    init { fetchInitialMovies() }



    fun fetchInitialMovies() {
        if (!isInitialLoad) return
        viewModelScope.launch {
            val result = movieRepository.getAllMovies() ?: emptyList()
            _movies.value = result
            isInitialLoad = false
        }
    }

    fun setupPusherConnection(context: Context) {
        if (moviePusherService == null) {
            moviePusherService = MoviePusherService(context) { action, movie ->
                handleMovieEvent(action, movie)
            }
        }
    }

    private fun handleMovieEvent(action: String, movie: Movie) {
        when (action) {
            "update" -> {
                _movies.postValue(_movies.value?.map {
                    if (it.movie_id == movie.movie_id) movie else it
                })
            }

            "create" -> {
                _movies.postValue(_movies.value.orEmpty() + movie)
            }

            "delete" -> {
                _movies.postValue(_movies.value?.filter {
                    it.movie_id != movie.movie_id
                })
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        moviePusherService?.stopPusher()
        moviePusherService = null
    }
}