package com.example.testapi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.testapi.R

@Composable
fun CardFavMovie(
    title: String,
    genres: String,
    duration: Int,
    status: String,
    averageRating: Float,
    poster_Url: String,
    movie_id: Int,
    onClick: (Int) -> Unit // Thêm listener để xử lý sự kiện click
) {
    val statusLabel = if (status == "1") "VIP" else "Free"
    val statusColor = if (status == "1")
        Color.Yellow.copy(alpha = 0.5f)
    else
        Color.Blue.copy(alpha = 0.5f)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = colorResource(R.color.black1))
                .padding(8.dp)
                .clickable { onClick(movie_id) }
        ) {
            // Poster phim
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(5.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = poster_Url),
                    contentDescription = "Movie poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Trạng thái
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .height(18.dp)
                        .width(28.dp)
                        .background(
                            color = statusColor,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusLabel,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }

            // Nội dung bên phải
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Thể loại: $genres",
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Thời lượng: ${duration} phút",
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "$averageRating",
                        fontSize = 13.sp,
                        color = Color(0xFFF5B041),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.star),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CardHisMovie(
    title: String,
    genres: String,
    progress: String,
    status: String,
    averageRating: Float,
    poster_Url: String,
    movie_id: Int,
    onClick: (Int) -> Unit
){
    val statusLabel = if (status == "1") "VIP" else "Free"
    val statusColor = if (status == "1")
        Color.Yellow.copy(alpha = 0.5f)
    else
        Color.Blue.copy(alpha = 0.5f)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = colorResource(R.color.black1))
                .padding(8.dp)
                .clickable { onClick(movie_id) }
        ) {
            // Poster phim
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(5.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = poster_Url),
                    contentDescription = "Movie poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Trạng thái
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .height(18.dp)
                        .width(28.dp)
                        .background(
                            color = statusColor,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusLabel,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }

            // Nội dung bên phải
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Thể loại: $genres",
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Đã xem đuợc: $progress",
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "$averageRating",
                        fontSize = 13.sp,
                        color = Color(0xFFF5B041),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.star),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}


