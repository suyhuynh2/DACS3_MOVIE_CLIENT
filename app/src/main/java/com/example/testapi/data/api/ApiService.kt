package com.example.testapi.data.api

import com.example.testapi.data.mode_data.Genres
import com.example.testapi.data.mode_data.Movie
import com.example.testapi.data.mode_data.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    //Movie
    @GET("movies-all")
    suspend fun getAllMovies(): Response<List<Movie>?>


    //Genres
    @GET("genres-all")
    suspend fun getAllGenres(): Response<List<Genres>?>

    //Users
    @POST("users")
    suspend fun createUser(@Body user: Users): Response<Users>

}