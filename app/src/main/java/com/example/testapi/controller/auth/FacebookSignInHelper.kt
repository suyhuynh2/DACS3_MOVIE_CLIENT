package com.example.testapi.controller.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookSignInHelper(private val activity: Activity) {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    companion object {
        const val RC_FACEBOOK_SIGN_IN = 9002
    }

    init {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("FacebookSignIn", "Login successful: ${result.accessToken.token}")
                onResult?.invoke(result.accessToken.token, null)
            }

            override fun onCancel() {
                Log.d("FacebookSignIn", "Login canceled")
                onResult?.invoke(null, Exception("Đăng nhập Facebook bị hủy"))
            }

            override fun onError(error: FacebookException) {
                Log.e("FacebookSignIn", "Login failed: ${error.message}")
                onResult?.invoke(null, error)
            }
        })
    }

    private var onResult: ((String?, Exception?) -> Unit)? = null

    fun signIn(onResult: (String?, Exception?) -> Unit) {
        this.onResult = onResult
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile"))
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_FACEBOOK_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun signOut(onComplete: () -> Unit) {
        LoginManager.getInstance().logOut()
        onComplete()
    }
}