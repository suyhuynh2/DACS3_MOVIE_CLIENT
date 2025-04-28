package com.example.testapi.ui.screens.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.ui.components.BigTitle
import com.example.testapi.ui.components.FilmItem
import com.example.testapi.ui.components.ListGenres
import com.example.testapi.viewmodel.GenresViewModel
import com.example.testapi.viewmodel.MovieViewModel

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MenuMoviePreview() {
    MenuMovieScreen()
}

@Composable
fun MenuMovieScreen(
    movieViewModel: MovieViewModel = viewModel(),
    genresViewModel: GenresViewModel = viewModel()
) {
    // Lấy dữ liệu từ ViewModel
    val movieListState = movieViewModel.movies.observeAsState(initial = emptyList())
    val movieList = movieListState.value ?: emptyList()

    val genresListState = genresViewModel.genres.observeAsState(initial = emptyList())
    val genresList = genresListState.value ?: emptyList()

    val isLoading = movieViewModel.isLoading.observeAsState(false)
    val error = movieViewModel.error.observeAsState()

    // Đảm bảo fetch dữ liệu phim
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        genresViewModel.setupPusherConnection(context)
        movieViewModel.setupPusherConnection(context)
    }

    // Giao diện danh sách thể loại và phim
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        LazyColumn {
            item {
                BigTitle(
                    title = "Kho Phim",
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            item {
                Divider(
                    color = Color(0xFF5B5353),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            item {
                // Hiển thị danh sách thể loại theo dữ liệu từ GenresViewModel
                ListGenres(
                    genres = listOf("Toàn bộ khu vực", "Việt Nam", "Nhật Bản", "Hàn Quốc", "Mỹ", "Trung Quốc"),
                    selectedGenres = genresViewModel.selectedRegion,
                    onGenresSelected = { selected -> genresViewModel.selectedRegion = selected },
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                )
            }

            item {
                // Hiển thị danh sách thể loại từ GenresViewModel
                ListGenres(
                    genres = listOf("Toàn bộ thể loại") + genresList.map { it.name },
                    selectedGenres = genresViewModel.selectedGenre,
                    onGenresSelected = { selected -> genresViewModel.selectedGenre = selected },
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                )
            }

            item {
                // Hiển thị các danh sách genres khác
                ListGenres(
                    genres = listOf("Toàn bộ các loại trả phí", "Free", "VIP"),
                    selectedGenres = genresViewModel.selectedPayment,
                    onGenresSelected = { selected -> genresViewModel.selectedPayment = selected },
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                )
            }

            item {
                ListGenres(
                    genres = listOf("Toàn bộ các thập niên", "2025", "2024", "2023", "2022", "2021"),
                    selectedGenres = genresViewModel.selectedYear,
                    onGenresSelected = { selected -> genresViewModel.selectedYear = selected },
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                )
            }

            item {
                ListGenres(
                    genres = listOf("Độ hot", "Mới nhất"),
                    selectedGenres = genresViewModel.selectedHotness,
                    onGenresSelected = { selected -> genresViewModel.selectedHotness = selected },
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                )
            }

            item {
                Divider(
                    color = Color(0xFF5B5353),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            }

            item {
                // Hiển thị danh sách phim
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(movieList) { movie ->
                        FilmItem(
                            movie_id = movie.movie_id, // Thêm movie_id
                            title = movie.title,
                            poster_url = movie.poster_url,
                            status = movie.status ?: "Free",
                            averageRating = movie.averageRating ?: 0f,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

