package com.example.testapi.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = TextStyle(
            color = Color(0xffffc107),
            fontSize = 20.sp
        ),
        modifier = modifier
            .padding(start = 16.dp, top = 8.dp),
        fontWeight = FontWeight.Bold
    )
}
@Composable
fun BigTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = TextStyle(
            color = Color(0xffffc107),
            fontSize = 26.sp
        ),
        modifier = modifier
            .padding(start = 16.dp, top = 8.dp),
        fontWeight = FontWeight.Bold
    )
}

@Preview(showBackground = true, backgroundColor = 0x00000000)
@Composable
fun SectionTitlePreview() {
    SectionTitle(title = "Phim Nổi Bật")
}
