package com.example.testapi.data.mode_data

data class Comment(
    var comment_id: Int,
    var movie_id: Int,
    var firebase_uid: String,
    var content: String,
    var rating: Int,
)
