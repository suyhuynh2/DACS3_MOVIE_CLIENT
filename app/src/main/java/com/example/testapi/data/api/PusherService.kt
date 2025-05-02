package com.example.testapi.data.api

import android.content.Context
import android.util.Log
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.channel.Channel
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.channel.PusherEvent

open class PusherService(private val context: Context, private val channelName: String) {

    protected val pusher: Pusher
    protected val channel: Channel

    init {
        val options = PusherOptions().apply {
            setCluster("ap1")
        }
        pusher = Pusher("44c13d55ef7b3fa1312f", options)
        channel = pusher.subscribe(channelName)
    }

    open fun startPusher() {
        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
            }

            override fun onError(message: String, code: String?, e: Exception) {
                Log.e("Pusher", "Error: $message, Code: ${code ?: "Unknown"}")
            }
        }, ConnectionState.ALL)
    }


    open fun stopPusher() {
        pusher.disconnect()
    }

    open fun subscribeToGenresUpdates() {}

    protected fun bindToChannel(eventName: String, onEvent: (data: String) -> Unit) {
        channel.bind(eventName, object : SubscriptionEventListener {
            override fun onEvent(event: PusherEvent?) {
                try {
                    val data = event?.data
                    if (data != null) {
                        onEvent(data)
                    }
                } catch (e: Exception) {
                    Log.e("Pusher", "Error processing event data: ${e.message}")
                }
            }
        })
    }
}
