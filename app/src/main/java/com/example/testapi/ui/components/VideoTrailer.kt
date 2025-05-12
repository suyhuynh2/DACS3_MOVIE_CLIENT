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

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoTrailer(
    modifier: Modifier = Modifier,
    videoUrl: String,
    isPlayingVideo: Boolean,
    posterUrl: String,
    initialProgress: String? = null, // Thêm tham số cho tiến độ ban đầu
    onProgressUpdate: (String) -> Unit
) {
    val context = LocalContext.current

    // Xây dựng URL luồng với xử lý phần mở rộng .mp4
    val streamUrl = remember(videoUrl) {
        val nameWithExt = if (videoUrl.endsWith(".mp4")) videoUrl else "$videoUrl.mp4"
        "http://192.168.1.10:8080/movie-trailer/$nameWithExt"
    }

    var isVideoReady by remember { mutableStateOf(false) }

    // Khởi tạo ExoPlayer với cài đặt bộ điều khiển có điều kiện
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

    // Chuyển đổi initialProgress sang milliseconds
    val initialPosition = remember(initialProgress) {
        initialProgress?.let { parseHMSToMillis(it) } ?: 0L
    }

    // Chuẩn bị mục phương tiện và phát lại
    LaunchedEffect(streamUrl, isPlayingVideo, initialPosition) {
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
        exoPlayer.prepare()
        if (initialPosition > 0) {
            exoPlayer.seekTo(initialPosition) // Đặt vị trí bắt đầu
        }
        exoPlayer.playWhenReady = true
        exoPlayer.volume = if (isPlayingVideo) 1f else 0f
    }

    // Giải phóng ExoPlayer khi thoát
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
                    useController = isPlayingVideo
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    setShowFastForwardButton(isPlayingVideo)
                    setShowRewindButton(isPlayingVideo)
                    setFullscreenButtonClickListener { isFullScreen ->
                        // Xử lý chuyển đổi toàn màn hình
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { playerView ->
                playerView.useController = isPlayingVideo
                playerView.setShowFastForwardButton(isPlayingVideo)
                playerView.setShowRewindButton(isPlayingVideo)
                playerView.setShowNextButton(false)
                playerView.setShowPreviousButton(false)
            }
        )

        // Hiển thị poster cho đến khi video sẵn sàng
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

private fun formatMillisToHMS(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

private fun parseHMSToMillis(hms: String): Long {
    val parts = hms.split(":")
    if (parts.size != 3) return 0L
    val hours = parts[0].toLongOrNull() ?: 0L
    val minutes = parts[1].toLongOrNull() ?: 0L
    val seconds = parts[2].toLongOrNull() ?: 0L
    return (hours * 3600 + minutes * 60 + seconds) * 1000
}