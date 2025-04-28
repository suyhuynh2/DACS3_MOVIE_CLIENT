package com.example.testapi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.R
import com.example.testapi.data.mode_data.Comment

@Composable
fun SectionComment(comments: List<Comment>, onCommentSubmit: (Comment) -> Unit) {
    var commentText by remember { mutableStateOf(TextFieldValue()) }
    var rating by remember { mutableStateOf(3) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        // Box chứa danh sách bình luận với thanh cuộn
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
                items(comments) { comment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Tên người dùng và số sao
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = "${comment.firebase_uid} - ${comment.rating} ★",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Yellow,
                                fontSize = 16.sp
                            )
                            Text(
                                text = comment.content,
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

        // Phần chọn số sao
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
                IconButton(onClick = { rating = i }) {
                    Icon(
                        painter = painterResource(
                            if (i <= rating) R.drawable.star else R.drawable.border_star
                        ),
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Phần nhập bình luận và nút gửi
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
                onClick = {},
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
    }
}