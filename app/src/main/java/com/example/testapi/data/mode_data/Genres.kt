package com.example.testapi.data.mode_data

import kotlinx.serialization.Serializable

@Serializable
data class Genres(
    var genres_id: Int,
    var name: String = "",
    var description: String = "",
)
