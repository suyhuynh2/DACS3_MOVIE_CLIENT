package com.example.testapi.ui.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.controller.auth.AuthManager
import com.example.testapi.firebase.LoginHandler
import com.example.testapi.ui.components.GradientButtonWithIcon
import com.example.testapi.ui.components.SystemUIWrapper
import com.example.testapi.ui.screens.screen.MainScreenActivity
import com.example.testapi.R

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authManager = AuthManager(this)
        val currentUser = authManager.getCurrentUser()

        if (currentUser != null) {
            // Nếu đã đăng nhập, chuyển thẳng đến MainScreen
            val loginHandler = LoginHandler(this)
            loginHandler.handleLogin(currentUser)
            return
        }

        setContent {
            SystemUIWrapper {
                LoginScreen()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val authManager = AuthManager(this)
        val loginHandler = LoginHandler(this)

        authManager.handleActivityResult(
            requestCode,
            data,
            onSuccess = { firebaseUser ->
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                loginHandler.handleLogin(firebaseUser)
            },
            onFailure = { error ->
                Toast.makeText(this, "Lỗi đăng nhập: $error", Toast.LENGTH_LONG).show()
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

@Composable
fun LoginScreen() {

    val context = LocalContext.current
    val authManager = remember { AuthManager(activity = context as Activity) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.black))
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Đăng nhập vào Dạo",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Đăng nhập để xem video với chất lượng cao",
                    style = TextStyle(
                        color = colorResource(R.color.green),
                        fontSize = 15.sp,
                    )
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            GradientButtonWithIcon(
                text = "Đăng nhập bằng Google",
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = {
                    authManager.signInWithGoogle(
                        onSuccess = { user ->
                            val intent = Intent(context, MainScreenActivity::class.java).apply {
                                putExtra("userName", "user${(1000..9999).random()}")
                            }
                            context.startActivity(intent)
                        },
                        onFailure = { exception ->
                            Toast.makeText(context, "Lỗi: $exception", Toast.LENGTH_LONG).show()
                            Log.e("LoginError", "Đăng nhập thất bại: $exception")
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            GradientButtonWithIcon(
                text = "Đăng nhập bằng Facebook",
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_facebook),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            GradientButtonWithIcon(
                text = "Đăng nhập bằng Twitter",
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_twitter),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(200.dp))

            val isChecked = remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    modifier = Modifier.size(16.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(id = R.color.green),
                        uncheckedColor = Color.Gray
                    )
                )

                Text(
                    text = buildAnnotatedString {
                        append("Tôi đã đủ 13 tuổi. Bằng việc nhấn Tiếp tục, bạn hiểu rõ, đồng ý rằng bạn đã đọc và chấp nhận bị rằng buộc bởi ")
                        withStyle(style = SpanStyle(color = Color(0xFF89CFF0))) {
                            append("Điều khoản sử dụng")
                        }
                        append(" và ")
                        withStyle(style = SpanStyle(color = Color(0xFF89CFF0))) {
                            append("Chính sách riêng tư")
                        }
                        append(".")
                    },
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
            }
        }
    }
}