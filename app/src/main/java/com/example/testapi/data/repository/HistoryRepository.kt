package com.example.testapi.data.repository

import com.example.testapi.data.api.ApiClient
import com.example.testapi.data.mode_data.History

class HistoryRepository {

    private val apiService = ApiClient.apiService

    suspend fun addHistory(history: History): Result<Unit> {
        return try {
            val response = apiService.addHistory(history)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Thêm lịch sử thất bại: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkHistory(firebaseUid: String, movieId: Int): Result<Boolean> {
        return try{
            val response = apiService.checkHistory(firebaseUid, movieId)
            if (response.isSuccessful) {
                val isHistory = response.body()?.is_history ?: false
                Result.success(isHistory)
            } else {
                Result.failure(Exception("Lỗi khi kiểm tra lịch sử: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistoryByUser(firebaseUid: String): List<History>? {
        return try {
            val response = apiService.getHistoryByUser(firebaseUid)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getInfoHistory(firebaseUid: String, movieId: Int): Result<History?> {
        return try {
            val response = apiService.getInfoHistory(firebaseUid, movieId)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Lỗi khi lấy lịch sử: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}