package com.example.testapi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.testapi.R

@Composable
fun ListGenres(
    genres: List<String>,
    modifier: Modifier = Modifier,
    selectedGenres: String,
    onGenresSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        ) {
            genres.forEach { genres ->
                GenresChip(genresName = genres, isSelected = genres == selectedGenres) {
                    onGenresSelected(genres)
                }
            }
        }
    }
}

@Composable
fun GenresChip(genresName: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) colorResource(R.color.black3) else colorResource(R.color.black)
    val contentColor = if (isSelected) colorResource(R.color.green_light) else Color.White

    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(start = 8.dp),
        shape = RoundedCornerShape(5.dp),
        color = backgroundColor
    ) {
        Text(
            text = genresName,
            modifier = Modifier.padding(6.dp),
            color = contentColor,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewListGenresComponent() {
    ListGenres(
        genres = listOf("All", "Drama", "Crime", "Action", "Comedy", "Adventure"),
        selectedGenres = "All",
        onGenresSelected = {}
    )
}