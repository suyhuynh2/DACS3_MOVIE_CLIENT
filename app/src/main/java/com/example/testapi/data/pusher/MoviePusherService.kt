package com.example.testapi.data.pusher

import android.content.Context
import android.util.Log
import com.example.testapi.data.api.PusherService
import com.example.testapi.data.mode_data.Movie
import com.google.gson.Gson
import org.json.JSONObject

class MoviePusherService(
    context: Context,
    private val onMovieEvent: (action: String, movie: Movie) -> Unit
) : PusherService(context, "movies-channel") {

    init {
        startPusher()  // Khởi động Pusher
        subscribeToUpdates()  // Lắng nghe các sự kiện cập nhật phim
    }

    private fun subscribeToUpdates() {
        bindToChannel("movies-updated") { raw ->
            Log.i("MoviePusherService", "Received raw data: $raw")
            try {
                val json = JSONObject(raw)
                val action = json.getString("action")

                // Parse TOÀN BỘ JSON thành Movie (trừ trường "action")
                val movieJson = JSONObject(raw).apply {
                    remove("action") // Loại bỏ trường action để tránh conflict
                }

                val movie = Gson().fromJson(movieJson.toString(), Movie::class.java)
                Log.i("MoviePusherService", "Parsed movie: $movie")

                // Gọi callback
                onMovieEvent(action, movie)
            } catch (e: Exception) {
                Log.e("MoviePusherService", "Error parsing event data: ${e.message}", e)
            }
        }
    }
}
