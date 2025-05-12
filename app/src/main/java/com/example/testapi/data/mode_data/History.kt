package com.example.testapi.data.mode_data

import java.util.Date

data class History(
    var history_id: Int? = null,
    var firebase_uid: String,
    var movie_id: Int,
    var progress: String,
    var watched_at: String? = null
)

data class CheckHistoryResponse(
    val is_history: Boolean
)
