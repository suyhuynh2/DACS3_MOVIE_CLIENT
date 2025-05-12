package com.example.testapi.ui.screens.hismovie

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.R
import com.example.testapi.ui.components.BigTitle
import com.example.testapi.ui.components.SystemUIWrapper
import com.example.testapi.viewmodel.ListHistoryViewModel
import com.example.testapi.viewmodel.ListHistoryViewModelFactory
import com.example.testapi.viewmodel.MovieViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.text.style.TextAlign
import com.example.testapi.ui.components.CardHisMovie
import com.example.testapi.ui.components.SearchBar
import com.example.testapi.ui.screens.detailmovie.DetailMovieActivity

class ListHistoryMovie : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemUIWrapper {
                ListHistoryMovieScreen()
            }
        }
    }
}

@Composable
fun ListHistoryMovieScreen() {
    val content = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firebaseUid = currentUser?.uid

    val historyViewModel: ListHistoryViewModel = viewModel(
        factory = ListHistoryViewModelFactory(firebaseUid ?: "")
    )
    val movieViewModel: MovieViewModel = viewModel()

    val historyList = historyViewModel.history.observeAsState(emptyList())
    val movieList = movieViewModel.movies.observeAsState(emptyList())

    LaunchedEffect(firebaseUid) {
        movieViewModel.setupPusherConnection(content)
        if (firebaseUid != null) {
            historyViewModel.fetchInitialHistoryMovie(firebaseUid)
            movieViewModel.fetchInitialMovies()
        }
    }

    val filteredMovies = if (historyList.value.isNotEmpty() && movieList.value.isNotEmpty()) {
        movieList.value.filter { movie ->
            historyList.value.any { it.movie_id == movie.movie_id }
        }
    } else {
        emptyList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        item {
            BigTitle(
                title = "Danh sách lịch sử",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            SearchBar(
                hint = "Tìm kiếm phim...",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            Divider(
                color = Color(0xFF5B5353),
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }

        if (firebaseUid == null) {
            item {
                Text(
                    text = "Bạn cần đăng nhập để xem lịch sử xem phim",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(filteredMovies) { movie ->
                val progress =
                    historyList.value.find { it.movie_id == movie.movie_id }?.progress ?: "00:00:00"
                Column {
                    CardHisMovie(
                        title = movie.title,
                        genres = movie.genres?.joinToString(", ") { it.name } ?: "",
                        progress = progress,
                        status = movie.status,
                        averageRating = movie.averageRating ?: 0f,
                        poster_Url = movie.poster_url,
                        movie_id = movie.movie_id,
                        onClick = {
                            val intent = Intent(content, DetailMovieActivity::class.java).apply {
                                putExtra("movie_id", movie.movie_id)
                                putExtra("title", movie.title)
                                putExtra("trailer_url", movie.trailer_url)
                                putExtra("status", movie.status)
                                putExtra("averageRating", movie.averageRating ?: 0f)
                                putExtra("description", movie.description)
                                putExtra("duration", movie.duration)
                                putExtra("actors", movie.actors)
                                putExtra("genres", movie.genres?.joinToString(", ") { it.name })
                                putExtra("poster_url", movie.poster_url)
                            }
                            content.startActivity(intent)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (filteredMovies.isEmpty()) {
                item {
                    Text(
                        text = "Không có lịch sử nào",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}