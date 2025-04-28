package com.example.testapi.ui.screens.detailmovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.R
import com.example.testapi.data.mode_data.Comment
import com.example.testapi.ui.components.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.media3.common.util.UnstableApi

class DetailMovieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movie_id = intent.getIntExtra("movie_id", 0)
        val title = intent.getStringExtra("title")
        val trailer_url = intent.getStringExtra("trailer_url")
        val status = intent.getStringExtra("status")
        val averageRating = intent.getFloatExtra("averageRating", 0f)
        val description = intent.getStringExtra("description")
        val duration = intent.getIntExtra("duration", 0)
        val actors = intent.getStringExtra("actors")
        val genres = intent.getStringExtra("genres")
        val poster_url = intent.getStringExtra("poster_url")

        setContent {
            SystemUIWrapper {
                DetailMovieScreen(
                    movie_id = movie_id,
                    title = title,
                    trailer_url = trailer_url,
                    status = status,
                    averageRating = averageRating,
                    description = description,
                    duration = duration,
                    actors = actors,
                    genres = genres,
                    poster_url = poster_url
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
    status: String?,
    averageRating: Float?,
    description: String?,
    duration: Int?,
    actors: String?,
    genres: String?,
    poster_url: String?
) {
    val scrollState = rememberScrollState()

    Column( // Sử dụng Column thay cho LazyColumn để đảm bảo nội dung không bị tràn
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState) // Để nội dung cuộn
    ) {
        // Trailer video (giữ cố định ở trên)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
        ) {
            @UnstableApi
            VideoTrailer(
                videoUrl = trailer_url ?: "",
                posterUrl = poster_url ?: ""
            )

            // Nút quay lại
            IconButton(
                onClick = { /* TODO: Back */ },
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

            // Nút yêu thích
            IconButton(
                onClick = { /* TODO: Favorite */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 25.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_favorite_border_24),
                    contentDescription = "Favorite",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Tiêu đề, đánh giá và icon play
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title ?: "",
                    fontWeight = FontWeight(550),
                    color = Color.White,
                    fontSize = 24.sp,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
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

            // Icon Play
            IconButton(
                onClick = { /* TODO: Play action */ },
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

        // Mô tả
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

        // Thể loại
        SectionTitle(title = "Thể loại")
        GenreTagList(genres = genres?.split(",") ?: listOf("Không rõ"))

        // Diễn viên
        SectionTitle(title = "Diễn viên")
        CastTagList(castList = actors?.split(",") ?: listOf("Không rõ"))

        // Bình luận & Đánh giá
        SectionTitle(title = "Bình luận & Đánh giá")
        val comments = remember {
            mutableStateListOf(
                Comment(comment_id = 1, firebase_uid = "Linda", movie_id = 123, content = "Phim rất hay!", rating = 5),
                Comment(comment_id = 2, firebase_uid = "Perter", movie_id = 123, content = "Hiệu ứng đỉnh cao.", rating = 4)
            )
        }
        SectionComment(comments = comments) { newComment ->
            comments.add(newComment)
        }
    }
}
