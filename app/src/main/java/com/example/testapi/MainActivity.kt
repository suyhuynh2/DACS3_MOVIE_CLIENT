package com.example.testapi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.testapi.ui.screens.screen.MainScreenActivity
import com.google.firebase.auth.FirebaseAuth


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

