package com.example.testapi.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage

fun convertGoogleDriveLink(originalLink: String): String? {
    val regex = Regex("""/d/([a-zA-Z0-9_-]+)""")
    val match = regex.find(originalLink)
    val fileId = match?.groupValues?.get(1)

    return if (fileId != null) {
        "https://drive.google.com/uc?export=download&id=$fileId"
    } else {
        null
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoTrailer(
    modifier: Modifier = Modifier,
    videoUrl: String,
    posterUrl: String? // THÊM URL poster để hiển thị trước khi trailer sẵn sàng
) {
    val context = LocalContext.current
    val convertedUrl = remember(videoUrl) { convertGoogleDriveLink(videoUrl) }

    var isTrailerReady by remember { mutableStateOf(false) }

    if (convertedUrl == null) return

    // Tạo ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(convertedUrl))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            volume = 0f
            repeatMode = Player.REPEAT_MODE_ALL

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        isTrailerReady = true
                    }
                }
            })
        }
    }

    Box(modifier = modifier) {
        // Video Player View
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Poster sẽ hiển thị cho tới khi trailer sẵn sàng
        if (!isTrailerReady && !posterUrl.isNullOrEmpty()) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Poster",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

    // Release ExoPlayer khi composable bị hủy
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}

