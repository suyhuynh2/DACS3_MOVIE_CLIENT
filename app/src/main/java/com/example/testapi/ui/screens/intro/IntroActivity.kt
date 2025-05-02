package com.example.testapi.ui.screens.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.R
import com.example.testapi.ui.components.SystemUIWrapper
import com.example.testapi.ui.screens.screen.MainScreenActivity

class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemUIWrapper {
                IntroScreen(
                    onStartClicked = {
                        startActivity(Intent(this, MainScreenActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
fun IntroScreenPreview(){
    IntroScreen()
}

@Composable
fun IntroScreen(onStartClicked: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.blackBackground))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.intro_pic),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))

            Text(
                text = "Khám phá ngay",
                color = Color.White,
                fontSize = 26.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Phim bạn yêu thích",
                color = Color.Yellow,
                fontSize = 26.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Phim lên sóng mood lên theo",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Text(
                text = "Lướt phim theo thể loại, tìm kiếm tựa phim\nbạn muốn, hoặc khám phá gợi ý dành riêng cho bạn",
                color = colorResource(R.color.teal_700),
                fontSize = 17.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.padding(top = 70.dp))

            Button(
                onClick = onStartClicked,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Bắt đầu thôi",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}


