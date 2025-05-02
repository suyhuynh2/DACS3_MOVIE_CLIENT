package com.example.testapi.controller.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.testapi.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInHelper(private val activity: Activity) {
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        const val RC_GOOGLE_SIGN_IN = 9001
    }

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult(data: Intent?, onResult: (GoogleSignInAccount?, Exception?) -> Unit) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            Log.d("GoogleSignIn", "Account: ${account.email}, IdToken: ${account.idToken}")
            onResult(account, null)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed: ${e.statusCode}, ${e.message}")
            onResult(null, e)
        }
    }

    fun signOut(onComplete: () -> Unit) {
        googleSignInClient.signOut().addOnCompleteListener(activity) {
            onComplete()
        }
    }
}