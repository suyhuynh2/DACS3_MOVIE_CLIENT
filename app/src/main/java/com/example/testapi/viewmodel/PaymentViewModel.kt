package com.example.testapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.data.mode_data.PaymentData
import com.example.testapi.data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val paymentRepository: PaymentRepository
) : ViewModel() {
    private val _paymentUrl = MutableStateFlow<String?>(null)
    val paymentUrl: StateFlow<String?> = _paymentUrl

    private val _paymentData = MutableStateFlow<PaymentData?>(null)
    val paymentData: StateFlow<PaymentData?> = _paymentData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error



    fun createPayment(firebaseUid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val data = paymentRepository.createPayment(firebaseUid)
            _isLoading.value = false
            if (data != null) _paymentData.value = data
            else _error.value = "Tạo đơn thanh toán thất bại"
        }
    }

}