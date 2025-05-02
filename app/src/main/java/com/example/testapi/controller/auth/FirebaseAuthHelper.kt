package com.example.testapi.controller.auth

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithGoogleToken(idToken: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(auth.currentUser, null)
                } else {
                    onComplete(null, task.exception)
                }
            }
    }

    fun signInWithFacebookToken(accessToken: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        val credential = FacebookAuthProvider.getCredential(accessToken)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(auth.currentUser, null)
                } else {
                    onComplete(null, task.exception)
                }
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
}