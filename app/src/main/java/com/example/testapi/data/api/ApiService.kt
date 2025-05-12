package com.example.testapi.data.api

import com.example.testapi.data.mode_data.CheckFavoriteResponse
import com.example.testapi.data.mode_data.CheckHistoryResponse
import com.example.testapi.data.mode_data.Favorite
import com.example.testapi.data.mode_data.Genres
import com.example.testapi.data.mode_data.History
import com.example.testapi.data.mode_data.Movie
import com.example.testapi.data.mode_data.Rating
import com.example.testapi.data.mode_data.Users
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Streaming

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

    @PUT("users/{firebase_uid}")
    suspend fun updateUser(
        @Path("firebase_uid") firebaseUid: String,
        @Body user: Users
    ): Response<Users>

    //Favorite
    @POST("add-favorite")
    suspend fun addFavorite(@Body favorite: Favorite): Response<Unit>

    @POST("remove-favorite")
    suspend fun removeFavorite(@Body favorite: Favorite): Response<Unit>

    @GET("check-favorite/{firebase_uid}/{movie_id}")
    suspend fun checkFavorite(
        @Path("firebase_uid") firebaseUid: String,
        @Path("movie_id") movieId: Int
    ): Response<CheckFavoriteResponse>

    @GET("get-favorite/{firebase_uid}")
    suspend fun getFavoritesByUser(
        @Path("firebase_uid") firebaseUid: String
    ): Response<List<Favorite>?>


    //Rating
    @POST("add-rating")
    suspend fun addRating(@Body rating: Rating): Response<Unit>

    @GET("all-rating/{movie_id}")
    suspend fun getRatingsByMovie(@Path("movie_id") movieId: Int): Response<List<Rating>?>

    //History
    @POST("add-history")
    suspend fun addHistory(@Body history: History): Response<Unit>

    @GET("check-history/{firebase_uid}/{movie_id}")
    suspend fun checkHistory(
        @Path("firebase_uid") firebaseUid: String,
        @Path("movie_id") movieId: Int
    ): Response<CheckHistoryResponse>

    @GET("get-history/{firebase_uid}")
    suspend fun getHistoryByUser(
        @Path("firebase_uid") firebaseUid: String
    ): Response<List<History>?>

    @GET("get-info-history/{firebase_uid}/{movie_id}")
    suspend fun getInfoHistory(
        @Path("firebase_uid") firebaseUid: String,
        @Path("movie_id") movieId: Int
    ): Response<History?>

    //Video
    @Streaming
    @GET("stream-video/{videoName}")
    suspend fun streamVideo(
        @Path("videoName") videoName: String
    ): Response<ResponseBody>


}