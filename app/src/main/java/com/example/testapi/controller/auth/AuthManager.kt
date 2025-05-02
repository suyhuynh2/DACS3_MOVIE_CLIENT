package com.example.testapi.controller.auth

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser

class AuthManager(private val activity: Activity) {
    private val googleSignInHelper = GoogleSignInHelper(activity)
    private val facebookSignInHelper = FacebookSignInHelper(activity)
    private val firebaseAuthHelper = FirebaseAuthHelper()

    fun signInWithGoogle(onSuccess: (FirebaseUser) -> Unit, onFailure: (String) -> Unit) {
        try {
            activity.startActivityForResult(
                googleSignInHelper.getSignInIntent(),
                GoogleSignInHelper.RC_GOOGLE_SIGN_IN
            )
        } catch (e: Exception) {
            onFailure("Không thể mở màn hình đăng nhập Google: ${e.message}")
        }
    }

    fun signInWithFacebook(onSuccess: (FirebaseUser) -> Unit, onFailure: (String) -> Unit) {
        facebookSignInHelper.signIn { accessToken, error ->
            if (accessToken != null && error == null) {
                firebaseAuthHelper.signInWithFacebookToken(accessToken) { user, authError ->
                    if (user != null && authError == null) {
                        activity.runOnUiThread { onSuccess(user) }
                    } else {
                        activity.runOnUiThread {
                            onFailure("Xác thực Firebase thất bại: ${authError?.message}")
                        }
                    }
                }
            } else {
                activity.runOnUiThread {
                    onFailure("Đăng nhập Facebook thất bại: ${error?.message}")
                }
            }
        }
    }

    fun handleActivityResult(
        requestCode: Int,
        data: Intent?,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (requestCode == GoogleSignInHelper.RC_GOOGLE_SIGN_IN) {
            googleSignInHelper.handleSignInResult(data) { account, error ->
                if (account != null && error == null) {
                    firebaseAuthHelper.signInWithGoogleToken(account.idToken!!) { user, authError ->
                        if (user != null && authError == null) {
                            activity.runOnUiThread { onSuccess(user) }
                        } else {
                            activity.runOnUiThread {
                                onFailure("Xác thực Firebase thất bại: ${authError?.message}")
                            }
                        }
                    }
                } else {
                    activity.runOnUiThread {
                        onFailure("Đăng nhập Google thất bại: ${error?.message}")
                    }
                }
            }
        }
    }

    fun signOut(onComplete: () -> Unit) {
        try {
            firebaseAuthHelper.signOut()
            googleSignInHelper.signOut {
                activity.runOnUiThread(onComplete)
            }
        } catch (e: Exception) {
            // Log error but still call onComplete to ensure UI updates
            android.util.Log.e("AuthManager", "Sign out failed: ${e.message}")
            activity.runOnUiThread(onComplete)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuthHelper.getCurrentUser()
    }
}