package com.example.testapi.data.repository

import com.example.testapi.data.api.ApiClient
import com.example.testapi.data.mode_data.Genres

class GenresRepository {
    private val apiService = ApiClient.apiService

    suspend fun getAllGenres(): List<Genres>? {
        return try {
            val response = apiService.getAllGenres()
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