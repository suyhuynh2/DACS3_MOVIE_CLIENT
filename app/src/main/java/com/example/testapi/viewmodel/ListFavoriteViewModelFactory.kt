// ListFavoriteViewModelFactory.kt
package com.example.testapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListFavoriteViewModelFactory(
    private val firebaseUid: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListFavoriteViewModel::class.java)) {
            return ListFavoriteViewModel(firebaseUid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}