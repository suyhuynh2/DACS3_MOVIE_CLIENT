package com.example.testapi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.testapi.ui.screens.intro.IntroActivity
import com.example.testapi.ui.screens.screen.MainScreenActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("first_time", true)

        if (isFirstTime) {
            startActivity(Intent(this, MainScreenActivity::class.java))
        } else {
            startActivity(Intent(this, MainScreenActivity::class.java))
        }

        finish()
    }

}

