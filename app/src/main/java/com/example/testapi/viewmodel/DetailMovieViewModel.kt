package com.example.testapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.mode_data.Favorite
import com.example.testapi.data.mode_data.History
import com.example.testapi.data.mode_data.Rating
import com.example.testapi.data.repository.FavoriteRepository
import com.example.testapi.data.repository.HistoryRepository
import com.example.testapi.data.repository.RatingRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailMovieViewModel: ViewModel() {
    private val favoriteRepository = FavoriteRepository()
    private val ratingRepository = RatingRepository()
    private val historyRepository = HistoryRepository()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _isHistory = MutableStateFlow(false)
    val isHistory: StateFlow<Boolean> = _isHistory

    private val _historyProgress = MutableStateFlow<String?>(null)
    val historyProgress: StateFlow<String?> = _historyProgress

    private val _ratings = MutableStateFlow<List<Rating>>(emptyList())
    val ratings: StateFlow<List<Rating>> = _ratings

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _ratingSuccess = MutableStateFlow<Boolean?>(null)
    val ratingSuccess: StateFlow<Boolean?> = _ratingSuccess

    val userRole = MutableStateFlow("")

    fun addFavorite(firebaseUid: String, movieId: Int) {
        viewModelScope.launch {
            val result = favoriteRepository.addFavorite(
                Favorite(firebase_uid = firebaseUid, movie_id = movieId)
            )
            if (result.isSuccess) {
                _isFavorite.value = true
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun removeFavorite(firebaseUid: String, movieId: Int) {
        viewModelScope.launch {
            val result = favoriteRepository.removeFavorite(
                Favorite(firebase_uid = firebaseUid, movie_id = movieId)
            )
            if (result.isSuccess) {
                _isFavorite.value = false
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun checkFavorite(firebaseUid: String, movieId: Int) {
        viewModelScope.launch {
            val result = favoriteRepository.checkFavorite(firebaseUid, movieId)
            if (result.isSuccess) {
                _isFavorite.value = result.getOrNull() ?: false
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun fetchRatings(movieId: Int) {
        viewModelScope.launch {
            val result = ratingRepository.getRatingsByMovie(movieId)
            if (result.isSuccess) {
                _ratings.value = result.getOrDefault(emptyList())
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
            }
    }

    fun addRating(firebase_uid: String, movie_id: Int, score: Int, comment: String) {
        viewModelScope.launch {
            val result = ratingRepository.addRating(
                Rating(
                    firebase_uid = firebase_uid,
                    movie_id = movie_id,
                    score = score,
                    comment = comment
                )
            )
            _ratingSuccess.value = result.isSuccess
            if (!result.isSuccess) {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun addHistory(firebaseUid: String, movieId: Int, progress: String){
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentTime = dateFormat.format(Date())

            val result = historyRepository.addHistory(
                History(
                    firebase_uid = firebaseUid,
                    movie_id = movieId,
                    progress = progress,
                    watched_at = currentTime
                )
            )
            if (!result.isSuccess) {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun checkHistory(firebaseUid: String, movieId: Int) {
        viewModelScope.launch {
            val result = historyRepository.checkHistory(firebaseUid, movieId)
            if (result.isSuccess) {
                _isHistory.value = result.getOrNull() ?: false
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun fetchHistory(firebaseUid: String, movieId: Int) {
        viewModelScope.launch {
            val result = historyRepository.getInfoHistory(firebaseUid, movieId)
            if (result.isSuccess) {
                val history = result.getOrNull()
                _isHistory.value = history != null
                _historyProgress.value = history?.progress
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun fetchUserRole(uid: String) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(uid)
        database.child("role").get().addOnSuccessListener {
            userRole.value = it.getValue(String::class.java) ?: "FREE"
        }
    }

}