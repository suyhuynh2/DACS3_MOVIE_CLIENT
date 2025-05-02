package com.example.testapi.data.repository

import com.example.testapi.data.api.ApiClient
import com.example.testapi.data.mode_data.Movie

class MovieRepository {
    private val apiService = ApiClient.apiService

    suspend fun getAllMovies(): List<Movie>? {
        return try {
            val response = apiService.getAllMovies()
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