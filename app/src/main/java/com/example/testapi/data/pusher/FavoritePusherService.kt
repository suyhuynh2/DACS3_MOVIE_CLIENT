package com.example.testapi.data.pusher

import android.content.Context
import android.util.Log
import com.example.testapi.data.api.PusherService
import com.example.testapi.data.mode_data.Favorite
import com.google.gson.Gson
import org.json.JSONObject

class FavoritePusherService(
    context: Context,
    private val onFavoriteEvent: (action: String, favorite: Favorite) -> Unit
) : PusherService(context, "favorite-channel") {

    init {
        startPusher()
        subscribeToUpdates()
    }

    private fun subscribeToUpdates() {
        bindToChannel("favorite-updated") { raw ->
            Log.i("FavoritePusherService", "Received raw data: $raw")
            try {
                val json = JSONObject(raw)
                val action = json.getString("action")

                val favoriteJson = JSONObject(raw).apply {
                    remove("action")
                }

                val favorite = Gson().fromJson(favoriteJson.toString(), Favorite::class.java)
                Log.i("FavoritePusherService", "Parsed favorite: $favorite")

                onFavoriteEvent(action, favorite)
            } catch (e: Exception) {
                Log.e("FavoritePusherService", "Error parsing event data: ${e.message}", e)
            }
        }
    }

}