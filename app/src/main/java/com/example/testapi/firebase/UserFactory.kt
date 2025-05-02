package com.example.testapi.firebase

import com.example.testapi.data.mode_data.Users
import com.google.firebase.auth.FirebaseUser

object UserFactory {
    fun fromFirebaseUser(firebaseUser: FirebaseUser): Users {
        val randomName = "user${(1000..9999).random()}"
        val provider = firebaseUser.providerData.firstOrNull { it.providerId == "facebook.com" || it.providerId == "google.com" }?.providerId ?: "unknown"
        return Users(
            firebase_uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            name = randomName,
            provider = when (provider) {
                "facebook.com" -> "facebook"
                "google.com" -> "google"
                else -> provider
            },
            role = "FREE"
        )
    }
}
