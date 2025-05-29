package com.example.testapi.ui.screens.detailmovie

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.testapi.R
import android.view.View

class FullscreenVideoActivity : ComponentActivity() {

    private var exoPlayer: ExoPlayer? = null
    private var videoUrl: String = ""
    private var position: Long = 0L

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nhận tất cả dữ liệu từ intent
        videoUrl = intent.getStringExtra("video_url") ?: ""
        position = intent.getLongExtra("position", 0L)

        if (videoUrl.isEmpty()) {
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContent {
            val context = LocalContext.current
            var showBackButton by remember { mutableStateOf(false) }

            val player = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(Uri.parse(getFinalUrl(videoUrl))))
                    prepare()
                    seekTo(position)
                    playWhenReady = true
                    volume = 1f
                    addListener(object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            finish()
                        }
                    })
                }.also { exoPlayer = it }
            }

            DisposableEffect(Unit) {
                onDispose { player.release() }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            this.player = player
                            useController = true
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                            setControllerVisibilityListener(
                                PlayerView.ControllerVisibilityListener { visibility ->
                                    showBackButton = visibility == View.VISIBLE
                                }
                            )
                            setBackgroundColor(android.graphics.Color.BLACK)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                if (showBackButton) {
                    IconButton(
                        onClick = {
                            setResult(
                                RESULT_OK,
                                Intent().putExtra("position", player.currentPosition)
                            )
                            finish()
                        },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Log.d("FullscreenVideoActivity", "Xoay ngang")
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                Log.d("FullscreenVideoActivity", "Xoay dọc")
            }
        }
    }
    private fun getFinalUrl(url: String): String {
        val streamUrl = if (url.endsWith(".mp4")) url else "$url.mp4"
        return "http://192.168.4.195:8080/movie-trailer/$streamUrl"
    }

    override fun onBackPressed() {
        exoPlayer?.let {
            val resultIntent = Intent().apply {
                putExtra("position", it.currentPosition)
            }
            setResult(Activity.RESULT_OK, resultIntent)
        }
        super.onBackPressed()
    }

}
