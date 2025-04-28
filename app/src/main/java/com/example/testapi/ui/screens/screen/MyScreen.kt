package com.example.testapi.ui.screens.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.R
import com.example.testapi.ui.components.GradientButton
import com.example.testapi.ui.reponsive.ResponsiveContent
import com.example.testapi.ui.reponsive.ResponsiveLayoutConfig
import com.example.testapi.ui.screens.login.LoginActivity

@Composable
fun MyScreen() {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Người dùng",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Banner ưu đãi
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFFDAB9) // Màu peachpuff cho banner
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Ưu đãi thành viên mới",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tháng đầu 23,000 VND",
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4500)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .height(32.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Ưu đãi đặc biệt nạp VIP",
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            }
            // Menu items
            MenuItem(
                title = "Thông tin của tôi",
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.person_info),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            MenuItem(
                title = "Danh sách phim yêu thích",
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.fav),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            MenuItem(
                title = "Lịch sử xem",
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.time),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            MenuItem(
                title = "Nâng cấp tài khoản",
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_diamond_24),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            MenuItem(
                title = "Cài đặt",
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            MenuItem(
                title = "Phản ánh ý kiến",
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.send_mail),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            MenuItem(
                title = "Đăng nhập",
                onClick = {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.login),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            it()
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MyScreenPreview() {
    ResponsiveContent { config, layoutConfig ->
        MyScreen()
    }
}