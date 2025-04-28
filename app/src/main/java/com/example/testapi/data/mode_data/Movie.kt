package com.example.testapi.data.mode_data

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    var movie_id: Int,
    var title: String = "",
    var actors: String = "",
    var description: String = "",
    var poster_url: String = "",
    var trailer_url: String = "",
    var video_url: String = "",
    var release_year: Int,
    var duration: Int,
    var country: String = "",
    var views: Int = 0,
    var status: String,
    val genres: List<Genres>? = null,
    val favorites: List<Favorite>? = null,
    val ratings: List<Rating>? = null,
    val averageRating: Float? = 0f
)