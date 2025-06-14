package com.example.testapi.ui.screens.screen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.data.model_component.Screen
import com.example.testapi.ui.components.BottomNavigationBar
import com.example.testapi.ui.components.SystemUIWrapper
import com.example.testapi.R
import com.example.testapi.viewmodel.UserViewModel

class MainScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemUIWrapper {
                MainScreen()
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
fun MainScreen() {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()

    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val uid = prefs.getString("user_id", null)

    LaunchedEffect(uid) {
        uid?.let { userViewModel.setupPusherConnection(context, it) }
    }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onScreenSelected = { screen -> currentScreen = screen }
            )
        },
        backgroundColor = colorResource(R.color.black2)
    )
    { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
                .background(color = Color.Black)
        ) {
            when (currentScreen) {
                Screen.Home -> HomeScreen()
                Screen.Profile -> MyScreen()
                Screen.MenuMovie -> MenuMovieScreen()
            }
        }
    }
}

