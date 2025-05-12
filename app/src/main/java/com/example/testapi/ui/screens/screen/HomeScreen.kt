package com.example.testapi.ui.screens.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.R
import com.example.testapi.data.mode_data.Movie
import com.example.testapi.data.model_component.SlideData
import com.example.testapi.ui.components.FilmItem
import com.example.testapi.ui.components.SearchBar
import com.example.testapi.ui.components.SectionTitle
import com.example.testapi.ui.components.SlideComponentUniversal
import com.example.testapi.ui.screens.detailmovie.DetailMovieActivity
import com.example.testapi.viewmodel.MovieViewModel


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Composable
fun HomeScreen(viewModel: MovieViewModel = viewModel()) {
    val movieList by viewModel.movies.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState(initial = null)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setupPusherConnection(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
            // Ô tìm kiếm
            SearchBar(hint = "Tìm kiếm...")

            // Slider ảnh
            val slideRes = listOf(
                SlideData.Url("https://cinema.mu/wp-content/uploads/2019/07/banner-films.jpg"),
                SlideData.Local(R.drawable.poster_2),
                SlideData.Local(R.drawable.poster_3)
            )
            SlideComponentUniversal(slides = slideRes)

            // Tiêu đề khu vực phim
            SectionTitle(title = "Phim nổi bật")

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                error != null -> {
                    Text(
                        text = error ?: "Có lỗi xảy ra",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    LazyRow(modifier = Modifier.padding(8.dp)) {
                        items(
                            items = movieList,
                            key = { movie: Movie -> movie.movie_id }
                        ) { movie: Movie ->
                            FilmItem(
                                movie_id = movie.movie_id,
                                title = movie.title,
                                poster_url = movie.poster_url,
                                status = movie.status,
                                averageRating = movie.averageRating ?: 0f,
                                onClick = {
                                    val intent = Intent(context, DetailMovieActivity::class.java).apply {
                                        putExtra("movie_id", movie.movie_id)
                                        putExtra("title", movie.title)
                                        putExtra("trailer_url", movie.trailer_url)
                                        putExtra("video_url", movie.video_url)
                                        putExtra("poster_url", movie.poster_url)
                                        putExtra("status", movie.status)
                                        putExtra("averageRating", movie.averageRating ?: 0f)
                                        putExtra("description", movie.description)
                                        putExtra("duration", movie.duration)
                                        putExtra("actors", movie.actors)
                                        putExtra("genres", movie.genres?.joinToString(",") { it.name })
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
