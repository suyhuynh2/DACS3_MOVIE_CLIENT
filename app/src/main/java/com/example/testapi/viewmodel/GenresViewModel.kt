package com.example.testapi.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.Pusher.GenresPusherService
import com.example.testapi.data.mode_data.Genres
import com.example.testapi.data.repository.GenresRepository
import kotlinx.coroutines.launch

class GenresViewModel: ViewModel() {
    private val genresRepository = GenresRepository()

    private val _genres = MutableLiveData<List<Genres>>(emptyList())
    val genres: LiveData<List<Genres>> = _genres

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    var selectedRegion by mutableStateOf("Toàn bộ khu vực")
    var selectedGenre by mutableStateOf("Toàn bộ thể loại")
    var selectedPayment by mutableStateOf("Toàn bộ các loại trả phí")
    var selectedYear by mutableStateOf("Toàn bộ các thập niên")
    var selectedHotness by mutableStateOf("Mới nhất")

    private var isInitialLoad = true
    private var genresPusherService: GenresPusherService? = null

    init { fetchInitialGenres() }

    private fun fetchInitialGenres() {
        if (!isInitialLoad) return
        viewModelScope.launch {
            val result = genresRepository.getAllGenres() ?: emptyList()
            _genres.value = result
            isInitialLoad = false
        }
    }

    fun setupPusherConnection(context: Context) {
        if (genresPusherService == null) {
            genresPusherService = GenresPusherService(context) { action, genre ->
                handleGenreEvent(action, genre)
            }
        }
    }

    private fun handleGenreEvent(action: String, genre: Genres) {
        when (action) {
            "update" -> {
                _genres.postValue(_genres.value?.map {
                    if (it.genres_id == genre.genres_id) genre else it
                })
            }
            "create" -> {
                _genres.postValue(_genres.value.orEmpty() + genre)
            }
            "delete" -> {
                _genres.postValue(_genres.value?.filter {
                    it.genres_id != genre.genres_id
                })
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        genresPusherService?.stopPusher()  // Dọn dẹp kết nối Pusher
        genresPusherService = null        // Giúp GC thu hồi bộ nhớ
    }
}

