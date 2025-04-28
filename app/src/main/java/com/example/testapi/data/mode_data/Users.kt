package com.example.testapi.data.mode_data

import kotlinx.serialization.Serializable

@Serializable
data class Users(
    var firebase_uid: String,
    var email: String = "",
    var name: String = "",
    var provider: String = "",
    var role: String = ""
)
