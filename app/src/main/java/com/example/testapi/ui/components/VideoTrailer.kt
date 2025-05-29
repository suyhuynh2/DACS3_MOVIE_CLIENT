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
import kotlinx.coroutines.delay

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoTrailer(
    modifier: Modifier = Modifier,
    videoUrl: String,
    isPlayingVideo: Boolean,
    posterUrl: String,
    initialProgress: String? = null,
    initialPosition: Long = 0L,
    onProgressUpdate: (String) -> Unit,
    onFullScreenClick: (Long) -> Unit
) {
    val context = LocalContext.current

    val streamUrl = remember(videoUrl) {
        if (videoUrl.isEmpty()) "" else {
            val nameWithExt = if (videoUrl.endsWith(".mp4")) videoUrl else "$videoUrl.mp4"
            "http://192.168.4.195:8080/movie-trailer/$nameWithExt"
        }
    }

    var isVideoReady by remember { mutableStateOf(false) }
    var shouldAutoPlay by remember { mutableStateOf(true) } // Thêm biến điều khiển auto play

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            volume = if (isPlayingVideo) 1f else 0f
            repeatMode = Player.REPEAT_MODE_ALL
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        isVideoReady = true
                    }
                }
            })
        }
    }

    LaunchedEffect(isVideoReady, initialPosition) {
        if (isVideoReady && initialPosition > 0L) {
            exoPlayer.seekTo(initialPosition)
        }
    }

    LaunchedEffect(streamUrl, isPlayingVideo) {
        if (streamUrl.isNotEmpty()) {
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
            exoPlayer.prepare()

            // Xử lý seekTo chỉ khi có initialPosition hoặc initialProgress
            if (initialPosition > 0) {
                exoPlayer.seekTo(initialPosition)
            } else {
                initialProgress?.let { progress ->
                    exoPlayer.seekTo(parseHMSToMillis(progress))
                }
            }

            // Auto play trailer khi không phải video chính
            if (!isPlayingVideo && shouldAutoPlay) {
                exoPlayer.playWhenReady = true
                exoPlayer.volume = 0f
            } else {
                exoPlayer.playWhenReady = isPlayingVideo
                exoPlayer.volume = if (isPlayingVideo) 1f else 0f
            }
        }
    }

    // Xử lý khi isPlayingVideo thay đổi
    LaunchedEffect(isPlayingVideo) {
        if (isPlayingVideo) {
            exoPlayer.volume = 1f
            exoPlayer.playWhenReady = true
            shouldAutoPlay = false // Tắt auto play khi chuyển sang video chính
        } else {
            exoPlayer.volume = 0f
            // Không set playWhenReady = false để trailer vẫn chạy ngầm
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (isPlayingVideo) {
                onProgressUpdate(formatMillisToHMS(exoPlayer.currentPosition))
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val currentPosition = exoPlayer.currentPosition
            val progress = formatMillisToHMS(currentPosition)
            onProgressUpdate(progress)
            exoPlayer.release()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = isPlayingVideo // Chỉ hiển thị controller cho video chính
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    setShowFastForwardButton(isPlayingVideo)
                    setShowRewindButton(isPlayingVideo)
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    if (isPlayingVideo) {
                        setFullscreenButtonClickListener {
                            exoPlayer.pause()
                            onFullScreenClick(exoPlayer.currentPosition)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { playerView ->
                playerView.player = exoPlayer
                playerView.useController = isPlayingVideo
                playerView.setShowFastForwardButton(isPlayingVideo)
                playerView.setShowRewindButton(isPlayingVideo)
                if (isPlayingVideo) {
                    playerView.setFullscreenButtonClickListener {
                        exoPlayer.pause()
                        onFullScreenClick(exoPlayer.currentPosition)
                    }
                }
            }
        )

        if (!isVideoReady && posterUrl.isNotEmpty()) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun formatMillisToHMS(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun parseHMSToMillis(hms: String): Long {
    val parts = hms.split(":")
    return when (parts.size) {
        3 -> parts[0].toLong() * 3600000 + parts[1].toLong() * 60000 + parts[2].toLong() * 1000
        2 -> parts[0].toLong() * 60000 + parts[1].toLong() * 1000
        else -> 0L
    }
}