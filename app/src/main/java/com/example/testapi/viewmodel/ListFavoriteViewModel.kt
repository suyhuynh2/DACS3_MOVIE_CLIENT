package com.example.testapi.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.mode_data.Favorite
import com.example.testapi.data.pusher.FavoritePusherService
import com.example.testapi.data.repository.FavoriteRepository
import kotlinx.coroutines.launch

class ListFavoriteViewModel(
    private val firebaseUid: String
) : ViewModel() {

    private val favoriteRepository = FavoriteRepository()

    private val _favorite = MutableLiveData<List<Favorite>>(emptyList())
    val favorite: LiveData<List<Favorite>> = _favorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var isInitialLoad = true
    private var favoritePusherService: FavoritePusherService? = null

    init { fetchInitialFavoritesMovie(firebaseUid) }

    fun fetchInitialFavoritesMovie(firebaseUid: String) {
        if (!isInitialLoad) return
        viewModelScope.launch {
            val result = favoriteRepository.getFavoritesByUser(firebaseUid) ?: emptyList()
            _favorite.value = result
            isInitialLoad = false
        }
    }

    fun setupPusherConnection(context: Context) {
        if (favoritePusherService == null) {
            favoritePusherService = FavoritePusherService(context) { action, movie ->
                favoritePusherService(action, movie)
            }
        }
    }

    private fun favoritePusherService(action: String, favorite: Favorite) {
        when (action) {
            "create" -> {
                _favorite.postValue(_favorite.value.orEmpty() + favorite)
            }
            "delete" -> {
                _favorite.postValue(_favorite.value?.filter {
                    it.favorite_id != favorite.favorite_id
                })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        favoritePusherService?.stopPusher()
        favoritePusherService = null
    }

}
