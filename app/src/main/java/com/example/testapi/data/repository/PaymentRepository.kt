package com.example.testapi.data.repository

import com.example.testapi.data.api.ApiClient
import com.example.testapi.data.mode_data.PaymentData
import com.example.testapi.data.mode_data.PaymentRequest

class PaymentRepository {

    private val apiService = ApiClient.apiService

    suspend fun createPayment(firebaseUid: String): PaymentData? {
        return try {
            val response = apiService.createPayment(PaymentRequest(firebaseUid))
            if (response.isSuccessful) {
                response.body()?.data
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}