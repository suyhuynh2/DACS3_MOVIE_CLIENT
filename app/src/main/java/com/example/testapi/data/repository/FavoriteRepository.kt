package com.example.testapi.data.repository

import com.example.testapi.data.api.ApiClient
import com.example.testapi.data.mode_data.Favorite
import com.example.testapi.data.mode_data.Movie

class FavoriteRepository {
    private val apiService = ApiClient.apiService

    suspend fun addFavorite(favorite: Favorite): Result<Unit> {
        return try {
            val response = apiService.addFavorite(favorite)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Thêm yêu thích thất bại: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkFavorite(firebaseUid: String, movieId: Int): Result<Boolean> {
        return try {
            val response = ApiClient.apiService.checkFavorite(firebaseUid, movieId)
            if (response.isSuccessful) {
                val isFavorite = response.body()?.is_favorite ?: false
                Result.success(isFavorite)
            } else {
                Result.failure(Exception("Lỗi khi kiểm tra yêu thích: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavorite(favorite: Favorite): Result<Unit> {
        return try {
            val response = apiService.removeFavorite(favorite)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Xoá yêu thích thất bại: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoritesByUser(firebaseUid: String): List<Favorite>? {
        return try {
            val response = apiService.getFavoritesByUser(firebaseUid)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}

