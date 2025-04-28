package com.example.testapi.data.mode_data

import kotlinx.serialization.Serializable

@Serializable
data class Rating (
    var rating_id: Int,
    var movie_id: Int,
    var firebase_uid: String,
    var score: Int
)