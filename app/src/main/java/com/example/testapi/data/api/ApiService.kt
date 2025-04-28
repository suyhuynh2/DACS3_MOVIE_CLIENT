package com.example.testapi.data.api

import com.example.testapi.data.mode_data.Genres
import com.example.testapi.data.mode_data.Movie
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    //Movie
    @GET("movies-all")
    suspend fun getAllMovies(): Response<List<Movie>?>


    //Genres
    @GET("genres-all")
    suspend fun getAllGenres(): Response<List<Genres>?>
}