package com.example.testapi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.R
import com.example.testapi.data.mode_data.Rating
import com.example.testapi.viewmodel.DetailMovieViewModel

@Composable
fun SectionComment(
    ratings: List<Rating>,
    movie_id: Int,
    firebase_uid: String?,
    username: String,
    onCommentSubmit: (Rating) -> Unit,
    viewModel: DetailMovieViewModel,
    userRole: String, // Thêm tham số này
    movieStatus: String? // Thêm tham số này
) {
    var commentText by remember { mutableStateOf(TextFieldValue()) }
    var ratingScore by remember { mutableStateOf(3) }

    // Lắng nghe trạng thái khi gửi thành công
    val ratingSuccess by viewModel.ratingSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Kiểm tra điều kiện ẩn phần nhập bình luận
    val shouldHideInput = userRole == "FREE" && movieStatus == "1"

    // Cập nhật lại danh sách đánh giá nếu gửi thành công
    LaunchedEffect(ratingSuccess) {
        if (ratingSuccess == true) {
            // Gọi lại fetchRatings để tải lại danh sách đánh giá mới
            viewModel.fetchRatings(movie_id)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.black2)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(12.dp)
            ) {
                items(ratings) { rating ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${rating.username} - ${rating.score} ★",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Yellow,
                                fontSize = 16.sp
                            )
                            Text(
                                text = rating.comment ?: "",
                                color = Color.White,
                                fontSize = 14.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chỉ hiển thị phần nhập bình luận nếu không thuộc điều kiện ẩn
        if (!shouldHideInput) {
            Text(
                text = "Đánh giá của bạn",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 1..5) {
                    IconButton(onClick = { ratingScore = i }) {
                        Icon(
                            painter = painterResource(
                                if (i <= ratingScore) R.drawable.star else R.drawable.border_star
                            ),
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientTextField(
                    value = commentText.text,
                    onValueChange = { commentText = TextFieldValue(it) },
                    hint = "Viết bình luận của bạn",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                        .height(40.dp)
                )

                Button(
                    onClick = {
                        if (!commentText.text.isNullOrBlank() && firebase_uid != null) {
                            val newRating = Rating(
                                movie_id = movie_id,
                                firebase_uid = firebase_uid,
                                score = ratingScore,
                                comment = commentText.text,
                                username = username
                            )
                            // Gửi đánh giá lên server
                            viewModel.addRating(firebase_uid, movie_id, ratingScore, commentText.text)
                            commentText = TextFieldValue("") // Làm trống trường nhập
                            ratingScore = 3 // Reset điểm đánh giá
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC107),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.send),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        } else {
        }

        // Hiển thị lỗi nếu có
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


