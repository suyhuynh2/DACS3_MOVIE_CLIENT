package com.example.testapi.data.repository

import com.example.testapi.data.api.ApiClient
import com.example.testapi.data.mode_data.Users

class UsersRepository {

    private val apiService = ApiClient.apiService

    suspend fun createUser(user: Users): Result<Users> {
        return try {
            val response = apiService.createUser(user)
            if (response.isSuccessful) {
                val userResponse = response.body()
                Result.success(userResponse ?: user)
            } else {
                Result.failure(Exception("Server error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}