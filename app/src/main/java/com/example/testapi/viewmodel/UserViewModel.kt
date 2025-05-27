package com.example.testapi.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapi.data.mode_data.Users
import com.example.testapi.data.pusher.UserPusherService

class UserViewModel : ViewModel() {

    private var userPusherService: UserPusherService? = null
    private val _userLiveData = MutableLiveData<Users>()
    val userLiveData: LiveData<Users> get() = _userLiveData

    fun setupPusherConnection(context: Context, firebase_uid: String) {
        if (userPusherService == null) {
            userPusherService = UserPusherService(
                context = context,
                firebase_uid = firebase_uid,
                onUserEvent = { action, user ->
                    if (action == "updated") {
                        updateUserInSharedPreferences(context, user)
                        _userLiveData.postValue(user)
                    }
                }
            )
        }
        val currentUser = loadUserFromSharedPreferences(context)
        if (currentUser != null) {
            _userLiveData.value = currentUser
        }
    }

    private fun loadUserFromSharedPreferences(context: Context): Users? {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null) ?: return null
        val name = sharedPreferences.getString("user_name", null) ?: return null
        val email = sharedPreferences.getString("user_email", null) ?: return null
        val role = sharedPreferences.getString("user_role", null) ?: return null
        val provider = sharedPreferences.getString("user_provider", null) ?: return null
        return Users(firebase_uid = userId, name = name, email = email, role = role, provider = provider)
    }

    private fun updateUserInSharedPreferences(context: Context, user: Users) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val currentUserId = sharedPreferences.getString("user_id", null)

        if (user.firebase_uid == currentUserId) {
            with(sharedPreferences.edit()) {
                putString("user_id", user.firebase_uid)
                putString("user_name", user.name)
                putString("user_email", user.email)
                putString("user_role", user.role)
                putString("user_provider", user.provider)
                apply()
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        userPusherService?.stopPusher()
        userPusherService = null
    }
}
