package com.example.testapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.mode_data.Favorite
import com.example.testapi.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMovieViewModel: ViewModel() {
    private val favoriteRepository = FavoriteRepository()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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

}