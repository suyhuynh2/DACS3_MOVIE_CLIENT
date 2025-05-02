package com.example.testapi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.testapi.R

@Composable
fun FilmItem(
    modifier: Modifier = Modifier,
    movie_id: Int,
    title: String,
    poster_url: String? = null,
    status: String? = null,
    averageRating: Float? = null,
    onClick: (Int) -> Unit = {} // Thêm onClick với movie_id
) {
    Column(
        modifier = modifier
            .width(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(movie_id) } // Truyền movie_id khi click
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.2f))
        ) {
            when {
                poster_url != null -> {
                    AsyncImage(
                        model = poster_url,
                        contentDescription = "Poster of $title",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Image",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            status?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = if (it == "Free") Color.Blue.copy(alpha = 0.3f) else Color.Yellow.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            averageRating?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "★ $it",
                        color = Color.Yellow,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x00000000)
@Composable
fun FilmItemPreview() {
    LazyRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(3) { index ->
            when (index) {
                0 -> FilmItem(
                    movie_id = 1,
                    title = "Sample Movie",
                    poster_url = "https://example.com/poster.jpg",
                    status = "Free",
                    averageRating = 7.5f
                )
                1 -> FilmItem(
                    movie_id = 2,
                    title = "Local Movie",
                    poster_url = "https://example.com/poster.jpg",
                    status = "Vip",
                    averageRating = 8.0f
                )
                2 -> FilmItem(
                    movie_id = 3,
                    title = "No Image Movie",
                    status = "Free",
                    averageRating = 6.5f
                )
            }
        }
    }
}