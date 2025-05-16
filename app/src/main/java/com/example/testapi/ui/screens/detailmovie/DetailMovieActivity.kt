package com.example.testapi.ui.screens.detailmovie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.ui.components.*
import com.example.testapi.R
import com.example.testapi.viewmodel.DetailMovieViewModel
import com.google.firebase.auth.FirebaseAuth

class DetailMovieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movie_id = intent.getIntExtra("movie_id", 0)
        val title = intent.getStringExtra("title")
        val video_url = intent.getStringExtra("video_url")
        val trailer_url = intent.getStringExtra("trailer_url")
        val status = intent.getStringExtra("status")
        val averageRating = intent.getFloatExtra("averageRating", 0f)
        val description = intent.getStringExtra("description")
        val duration = intent.getIntExtra("duration", 0)
        val actors = intent.getStringExtra("actors")
        val genres = intent.getStringExtra("genres")
        val poster_url = intent.getStringExtra("poster_url")
        val position = intent.getLongExtra("position", 0L) // Nhận position từ Intent

        setContent {
            SystemUIWrapper {
                DetailMovieScreen(
                    movie_id = movie_id,
                    title = title,
                    trailer_url = trailer_url,
                    video_url = video_url,
                    status = status,
                    averageRating = averageRating,
                    description = description,
                    duration = duration,
                    actors = actors,
                    genres = genres,
                    poster_url = poster_url,
                    initialPosition = position // Chuyển position thành định dạng HMS
                )
            }
        }
    }
}

@Composable
fun DetailMovieScreen(
    movie_id: Int,
    title: String?,
    trailer_url: String?,
    video_url: String?,
    status: String?,
    averageRating: Float?,
    description: String?,
    duration: Int?,
    actors: String?,
    genres: String?,
    poster_url: String?,
    initialPosition: Long = 0L // Thêm tham số initialPosition
) {
    val viewModel: DetailMovieViewModel = viewModel()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val isHistory by viewModel.isHistory.collectAsState()
    val historyProgress by viewModel.historyProgress.collectAsState()
    val ratings by viewModel.ratings.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firebase_uid = currentUser?.uid
    val username = currentUser?.displayName ?: "Ẩn danh"

    var isPlayingVideo by remember { mutableStateOf(false) }
    var showContinueDialog by remember { mutableStateOf(false) }
    var currentVideoPosition by remember { mutableStateOf(initialPosition) }

    val fullscreenLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newPosition = result.data?.getLongExtra("position", 0L) ?: 0L
            currentVideoPosition = newPosition
            isPlayingVideo = true
        }
    }

    LaunchedEffect(isPlayingVideo) {
        if (isPlayingVideo) {
            currentVideoPosition = parseHMSToMillis(historyProgress ?: "00:00:00")
        }
    }

    LaunchedEffect(movie_id) {
        viewModel.fetchRatings(movie_id)
    }

    LaunchedEffect(movie_id, currentUser) {
        currentUser?.uid?.let { uid -> viewModel.checkFavorite(uid, movie_id) }
    }

    LaunchedEffect(movie_id, currentUser) {
        currentUser?.uid?.let { uid ->
            viewModel.fetchHistory(uid, movie_id)
        }
    }

    if (showContinueDialog) {
        AlertDialog(
            onDismissRequest = { showContinueDialog = false },
            title = { Text("Tiếp tục xem?") },
            text = { Text("Bạn đã xem đến $historyProgress. Bạn có muốn tiếp tục từ vị trí này?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showContinueDialog = false
                        isPlayingVideo = true
                    }
                ) {
                    Text("Tiếp tục")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showContinueDialog = false
                        viewModel.addHistory(firebase_uid ?: "", movie_id, "00:00:00")
                    }
                ) {
                    Text("Xem từ đầu")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isPlayingVideo)
                        Modifier.aspectRatio(16f / 9f)
                    else
                        Modifier.height(260.dp)
                )
        ) {
            VideoTrailer(
                videoUrl = if (isPlayingVideo) video_url ?: "" else trailer_url ?: "",
                posterUrl = poster_url ?: "",
                isPlayingVideo = isPlayingVideo,
                initialProgress = if (isPlayingVideo) historyProgress else null,
                initialPosition = currentVideoPosition,
                onProgressUpdate = { progress ->
                    if (isPlayingVideo) {
                        currentVideoPosition = parseHMSToMillis(progress)
                        viewModel.addHistory(firebase_uid ?: "", movie_id, progress)
                    }
                },
                onFullScreenClick = { pos ->
                    if (isPlayingVideo) {
                        val intent = Intent(context, FullscreenVideoActivity::class.java).apply {
                            putExtra("video_url", video_url)
                            putExtra("position", pos)
                            // Truyền tất cả dữ liệu cần thiết
                            putExtra("movie_id", movie_id)
                            putExtra("title", title)
                            putExtra("trailer_url", trailer_url)
                            putExtra("status", status)
                            putExtra("averageRating", averageRating ?: 0f)
                            putExtra("description", description)
                            putExtra("duration", duration ?: 0)
                            putExtra("actors", actors)
                            putExtra("genres", genres)
                            putExtra("poster_url", poster_url)
                        }
                        fullscreenLauncher.launch(intent)
                    }
                }
            )

            IconButton(
                onClick = { activity?.finish() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 25.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(
                onClick = {
                    currentUser?.uid?.let { uid ->
                        if (isFavorite) viewModel.removeFavorite(uid, movie_id)
                        else viewModel.addFavorite(uid, movie_id)
                    } ?: run {
                        Toast.makeText(context, "Bạn cần đăng nhập để yêu thích", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 25.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite)
                            R.drawable.fav else R.drawable.baseline_favorite_border_24
                    ),
                    contentDescription = "Favorite",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title ?: "",
                    fontWeight = FontWeight(550),
                    color = Color.White,
                    fontSize = 24.sp,
                )

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = averageRating?.toString() ?: "",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                    )

                    Icon(
                        painter = painterResource(R.drawable.time),
                        contentDescription = "Time",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$duration phút",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            IconButton(
                onClick = {
                    if (!isPlayingVideo) {
                        if (isHistory && historyProgress != null) {
                            showContinueDialog = true
                        } else {
                            isPlayingVideo = true
                        }
                    }
                },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_play_circle_24),
                    contentDescription = "Play",
                    tint = Color.Red,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        SectionTitle(title = "Mô tả")
        Text(
            text = description ?: "",
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        SectionTitle(title = "Thể loại")
        GenreTagList(genres = genres?.split(",") ?: listOf("Không rõ"))

        SectionTitle(title = "Diễn viên")
        CastTagList(castList = actors?.split(",") ?: listOf("Không rõ"))

        SectionTitle(title = "Bình luận & Đánh giá")
        SectionComment(
            ratings = ratings,
            movie_id = movie_id,
            firebase_uid = firebase_uid,
            username = username,
            onCommentSubmit = { newRating ->
                viewModel.addRating(firebase_uid ?: "", movie_id, newRating.score, newRating.comment ?: "")
            },
            viewModel = viewModel
        )
    }
}