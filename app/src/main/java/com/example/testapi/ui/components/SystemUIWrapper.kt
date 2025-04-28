package com.example.testapi.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SystemUIWrapper(
    backgroundColor: Color = Color.Black,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = backgroundColor.luminance() > 0.5f
        )
        systemUiController.setNavigationBarColor(
            color = backgroundColor,
            darkIcons = backgroundColor.luminance() > 0.5f
        )
    }

    content()
}