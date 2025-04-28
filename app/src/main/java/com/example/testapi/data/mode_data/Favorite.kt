package com.example.testapi.data.mode_data

import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    var favorite_id: Int,
    var movie_id: Int,
    val firebase_uid: Int,
)
