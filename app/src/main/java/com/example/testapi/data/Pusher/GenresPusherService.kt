package com.example.testapi.data.Pusher

import android.content.Context
import android.util.Log
import com.example.testapi.data.api.PusherService
import com.example.testapi.data.mode_data.Genres
import com.google.gson.Gson
import org.json.JSONObject

class GenresPusherService(
    context: Context,
    private val onGenreEvent: (action: String, genre: Genres) -> Unit
) : PusherService(context, "genres-channel") {

    init {
        startPusher()
        subscribeToUpdates()
    }

    private fun subscribeToUpdates() {
        bindToChannel("genres-updated") { raw ->
            Log.i("GenresPusherService", "Received raw data: $raw")
            try {
                val json = JSONObject(raw)
                Log.i("GenresPusherService", "Parsed JSON: $json")
                val action = json.getString("action")
                val genre = Genres(
                    genres_id = json.getInt("genres_id"),
                    name = json.getString("name"),
                    description = json.getString("description")
                )
                Log.i("GenresPusherService", "Calling onGenreEvent with action: $action, genre: $genre")
                onGenreEvent(action, genre)
            } catch (e: Exception) {
                Log.e("GenresPusherService", "Error parsing event data: ${e.message}", e)
            }
        }
    }
}
