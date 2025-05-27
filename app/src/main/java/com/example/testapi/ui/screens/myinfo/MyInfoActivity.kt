package com.example.testapi.ui.screens.myinfo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.R
import com.example.testapi.data.mode_data.Users
import com.example.testapi.data.repository.UsersRepository
import com.example.testapi.ui.components.SystemUIWrapper
import kotlinx.coroutines.launch
import com.example.testapi.viewmodel.UserViewModel
import androidx.compose.runtime.livedata.observeAsState

class MyInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getStringExtra("user_id") ?: "Unknown"
        val userName = intent.getStringExtra("user_name") ?: "Unknown"
        val userEmail = intent.getStringExtra("user_email") ?: ""
        val userRole = intent.getStringExtra("user_role") ?: "FREE"
        val userProvider = intent.getStringExtra("user_provider") ?: "unknown"

        setContent {
            SystemUIWrapper {
                MyInfoScreen(
                    firebaseUid = userId,
                    initialUsername = userName,
                    email = userEmail,
                    role = userRole,
                    loginProvider = userProvider
                )
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .border(1.dp, Color.White, MaterialTheme.shapes.small)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MyInfoScreen(
    firebaseUid: String = "",
    initialUsername: String,
    email: String,
    role: String,
    loginProvider: String,
) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.userLiveData.observeAsState() // Quan sát LiveData

    // Khởi tạo các giá trị ban đầu
    var tempUsername by remember { mutableStateOf(initialUsername) }
    var currentUsername by remember { mutableStateOf(initialUsername) }
    var currentEmail by remember { mutableStateOf(email) }
    var currentRole by remember { mutableStateOf(role) }
    var currentProvider by remember { mutableStateOf(loginProvider) }

    // Cập nhật UI khi userLiveData thay đổi
    LaunchedEffect(user) {
        user?.let {
            currentUsername = it.name
            currentEmail = it.email
            currentRole = it.role
            currentProvider = it.provider
            tempUsername = it.name // Đồng bộ tempUsername với dữ liệu mới
        }
    }

    // Thiết lập kết nối Pusher
    LaunchedEffect(Unit) {
        userViewModel.setupPusherConnection(context, firebaseUid)
    }

    fun saveUserToSharedPreferences(user: Users) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("user_id", user.firebase_uid)
            putString("user_name", user.name)
            putString("user_email", user.email)
            putString("user_role", user.role)
            putString("user_provider", user.provider)
            apply()
        }
    }

    val activity = context as? Activity
    val scope = rememberCoroutineScope()
    val repository = remember { UsersRepository() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.black))
            .padding(top = 30.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { activity?.finish() }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Thông tin tài khoản",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Text(
                text = "Tên người dùng:",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp)
            )
            CustomOutlinedTextField(
                value = tempUsername,
                onValueChange = { tempUsername = it },
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            )

            Text(
                text = "Tài khoản:",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = currentEmail,
                color = Color.LightGray,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 32.dp)
            )

            Text(
                text = "Gói tài khoản:",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = currentRole,
                color = Color.LightGray,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 32.dp)
            )

            Text(
                text = "Đăng nhập bằng:",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = currentProvider,
                color = Color.LightGray,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 32.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    scope.launch {
                        val updatedUser = Users(
                            firebase_uid = firebaseUid,
                            email = currentEmail,
                            name = tempUsername,
                            provider = currentProvider,
                            role = currentRole
                        )
                        val result = repository.updateUser(updatedUser)
                        if (result.isSuccess) {
                            saveUserToSharedPreferences(updatedUser)
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                            activity?.finish()
                        } else {
                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f)
                    .height(35.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.green_light))
            ) {
                Text(
                    text = "Lưu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
