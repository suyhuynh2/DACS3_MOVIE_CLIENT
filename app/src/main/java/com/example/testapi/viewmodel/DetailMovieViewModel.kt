package com.example.testapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.mode_data.Favorite
import com.example.testapi.data.mode_data.Rating
import com.example.testapi.data.repository.FavoriteRepository
import com.example.testapi.data.repository.RatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMovieViewModel: ViewModel() {
    private val favoriteRepository = FavoriteRepository()
    private val ratingRepository = RatingRepository()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _ratings = MutableStateFlow<List<Rating>>(emptyList())
    val ratings: StateFlow<List<Rating>> = _ratings

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _ratingSuccess = MutableStateFlow<Boolean?>(null)
    val ratingSuccess: StateFlow<Boolean?> = _ratingSuccess

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
}