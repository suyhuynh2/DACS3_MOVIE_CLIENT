package com.example.testapi.ui.screens.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.controller.auth.AuthManager
import com.example.testapi.ui.screens.login.LoginActivity
import com.example.testapi.R
import com.example.testapi.ui.screens.favmovie.ListFavoriteMovie
import com.example.testapi.ui.screens.myinfo.MyInfoActivity

@Composable
fun MyScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var userName by remember { mutableStateOf(sharedPreferences.getString("user_name", "Người dùng") ?: "Người dùng") }
    val activity = context as Activity
    val authManager = remember { AuthManager(activity = activity) }
    val isLoggedIn = authManager.getCurrentUser() != null

    val listener = remember {
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == "user_name") {
                userName = prefs.getString("user_name", "Người dùng") ?: "Người dùng"
            }
        }
    }

    DisposableEffect(sharedPreferences) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

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
                    text = userName,
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
                onClick = {
                    val intent = Intent(context, MyInfoActivity::class.java).apply {
                        putExtra("user_id", sharedPreferences.getString("user_id", "Unknown"))
                        putExtra("user_name", sharedPreferences.getString("user_name", "Unknown"))
                        putExtra("user_email", sharedPreferences.getString("user_email", ""))
                        putExtra("user_role", sharedPreferences.getString("user_role", "FREE"))
                        putExtra("user_provider", sharedPreferences.getString("user_provider", "unknown"))
                    }
                    context.startActivity(intent)
                },
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
                onClick = {
                    val intent = Intent(context, ListFavoriteMovie::class.java)
                    context.startActivity(intent)
                },

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
                title = if (isLoggedIn) "Đăng xuất" else "Đăng nhập",
                onClick = {
                    if (isLoggedIn) {
                        authManager.signOut {

                            with(sharedPreferences.edit()) {
                                clear()
                                apply()
                            }

                            Toast.makeText(context, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, LoginActivity::class.java))
                            activity.finish() // Close MainScreenActivity
                        }
                    } else {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = if (isLoggedIn) R.drawable.logout else R.drawable.login),
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