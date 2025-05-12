package com.example.testapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.mode_data.History
import com.example.testapi.data.repository.HistoryRepository
import kotlinx.coroutines.launch

class ListHistoryViewModel(
    private val firebaseUid: String
) : ViewModel() {

    private val historyRepository = HistoryRepository()

    private val _history = MutableLiveData<List<History>>(emptyList())
    val history: LiveData<List<History>> = _history

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var isInitialLoad = true

    init { fetchInitialHistoryMovie(firebaseUid) }

    fun fetchInitialHistoryMovie(firebaseUid: String) {
        if (!isInitialLoad) return
        viewModelScope.launch {
            val result = historyRepository.getHistoryByUser(firebaseUid) ?: emptyList()
            _history.value = result
            isInitialLoad = false
        }
    }

}