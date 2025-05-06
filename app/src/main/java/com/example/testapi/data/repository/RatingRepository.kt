package com.example.testapi.data.repository

import com.example.testapi.data.api.ApiClient
import com.example.testapi.data.mode_data.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RatingRepository {
    private val apiService = ApiClient.apiService

    suspend fun addRating(rating: Rating): Result<Unit> {
        return try {
            val response = apiService.addRating(rating)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Gửi đánh giá thất bại"))
            }
        } catch (e: Exception) {
                Result.failure(e)
        }
    }

    suspend fun getRatingsByMovie(movieId: Int): Result<List<Rating>> {
        return try {
            val response = ApiClient.apiService.getRatingsByMovie(movieId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Không thể tải danh sách đánh giá"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}




