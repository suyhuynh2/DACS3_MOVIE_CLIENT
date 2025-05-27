package com.example.testapi.data.pusher

import android.content.Context
import android.util.Log
import com.example.testapi.data.api.PusherService
import com.example.testapi.data.mode_data.Users
import com.google.gson.Gson
import org.json.JSONObject

class UserPusherService(
    context: Context,
    private val firebase_uid: String,
    private val onUserEvent: (action: String, user: Users) -> Unit
) :  PusherService(context, "user-channel-$firebase_uid") {

    init {
        startPusher()
        subscribeToUpdates()
    }

    private fun subscribeToUpdates() {
        bindToChannel("user-updated") { raw ->
            Log.i("UserPusherService", "Received raw data: $raw")
            try {
                val json = JSONObject(raw)
                val action = json.getString("action")

                val userJson = JSONObject(raw).apply {
                    remove("action")
                }

                val user = Gson().fromJson(userJson.toString(), Users::class.java)
                Log.i("UserPusherService", "Parsed user: $user")

                onUserEvent(action, user)
            } catch (e: Exception) {
                Log.e("UserPusherService", "Error parsing event data: ${e.message}", e)
            }
        }
    }
}
