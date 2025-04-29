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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthGoogle(private val activity: Activity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private var onSignInSuccess: (() -> Unit)? = null
    private var onSignInFailure: ((String) -> Unit)? = null

    companion object {
        private const val TAG = "AuthGoogle"
        private const val RC_GOOGLE_SIGN_IN = 9001
    }

    private fun configureGoogleSign() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        this.onSignInSuccess = onSuccess
        this.onSignInFailure = onFailure

        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener(activity) {
            auth.signOut()
            Log.d(TAG, "User signed out")
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                onSignInFailure?.invoke("Đăng nhập Google thất bại: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    onSignInSuccess?.invoke()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    onSignInFailure?.invoke("Xác thực với Firebase thất bại: ${task.exception?.message}")
                }
            }
    }
}