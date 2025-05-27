package com.example.testapi.ui.screens.screen

import android.content.Intent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.ui.components.BigTitle
import com.example.testapi.ui.components.FilmItem
import com.example.testapi.ui.components.ListGenres
import com.example.testapi.ui.components.SearchBar
import com.example.testapi.ui.screens.detailmovie.DetailMovieActivity
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

    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

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
                SearchBar(
                    hint = "Tìm Kiếm phim",
                    modifier = Modifier.padding(top = 8.dp),
                    query = searchQuery,
                    onQueryChanged = setSearchQuery
                )
            }

            item {
                Divider(
                    color = Color(0xFF5B5353),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            item {
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
                    genres = listOf("Toàn bộ các thập niên", "2025", "2024", "2023", "2022", "2021", "2020", "Các năm trước"),
                    selectedGenres = genresViewModel.selectedYear,
                    onGenresSelected = { selected -> genresViewModel.selectedYear = selected },
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                )
            }

            item {
                ListGenres(
                    genres = listOf("Mới nhất", "Hot nhất"),
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
                val filteredMovies = movieList.filter { movie ->
                    val regionMatch = genresViewModel.selectedRegion == "Toàn bộ khu vực" ||
                            movie.country.equals(genresViewModel.selectedRegion, ignoreCase = true)

                    val genreMatch = genresViewModel.selectedGenre == "Toàn bộ thể loại" ||
                            movie.genres?.any { it.name.equals(genresViewModel.selectedGenre, ignoreCase = true) } == true

                    val paymentMatch = genresViewModel.selectedPayment == "Toàn bộ các loại trả phí" ||
                            (genresViewModel.selectedPayment == "VIP" && movie.status == "1") ||
                            (genresViewModel.selectedPayment == "Free" && movie.status == "0")

                    val yearMatch = when (genresViewModel.selectedYear) {
                        "Toàn bộ các thập niên" -> true
                        "Các năm trước" -> movie.release_year?.let { it < 2020 } == true
                        else -> movie.release_year?.toString()?.contains(genresViewModel.selectedYear) == true
                    }

                    val hotnessMatch = genresViewModel.selectedHotness == "Mới nhất" || genresViewModel.selectedHotness == "Hot nhất"

                    val searchMatch = searchQuery.isBlank() ||
                            movie.title.contains(searchQuery, ignoreCase = true)

                    regionMatch && genreMatch && paymentMatch && yearMatch && hotnessMatch && searchMatch
                }

                val sortedMovies = if (genresViewModel.selectedHotness == "Hot nhất") {
                    filteredMovies.sortedByDescending { (it.views ?: 0) + (it.averageRating ?: 0f).toInt() }
                } else {
                    filteredMovies
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(sortedMovies) { movie ->
                        FilmItem(
                            movie_id = movie.movie_id,
                            title = movie.title,
                            poster_url = movie.poster_url,
                            status = movie.status ?: "Free",
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

