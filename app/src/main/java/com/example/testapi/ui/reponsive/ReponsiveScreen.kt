package com.example.testapi.ui.reponsive

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Enum để xác định các loại kích thước màn hình
enum class ScreenSize {
    SMALL, MEDIUM, LARGE, EXTRA_LARGE
}

// Class để quản lý cấu hình responsive
data class ResponsiveConfig(
    val screenWidth: Dp,
    val screenHeight: Dp,
    val screenSize: ScreenSize
)

// Utility class để xác định các breakpoint
object ResponsiveBreakpoints {
    val smallMaxWidth = 600.dp
    val mediumMaxWidth = 840.dp
    val largeMaxWidth = 1200.dp

    fun getScreenSize(width: Dp): ScreenSize {
        return when {
            width <= smallMaxWidth -> ScreenSize.SMALL
            width <= mediumMaxWidth -> ScreenSize.MEDIUM
            width <= largeMaxWidth -> ScreenSize.LARGE
            else -> ScreenSize.EXTRA_LARGE
        }
    }
}

// Composable để lấy thông tin responsive
@Composable
fun rememberResponsiveConfig(): ResponsiveConfig {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val screenSize = ResponsiveBreakpoints.getScreenSize(screenWidth)

    return ResponsiveConfig(
        screenWidth = screenWidth,
        screenHeight = screenHeight,
        screenSize = screenSize
    )
}

// Class để quản lý layout responsive
data class ResponsiveLayoutConfig(
    val contentMaxWidth: Dp
)

// Object để cung cấp cấu hình layout
object ResponsiveLayoutProvider {
    fun getLayoutConfig(screenSize: ScreenSize): ResponsiveLayoutConfig {
        return when (screenSize) {
            ScreenSize.SMALL -> ResponsiveLayoutConfig(
                contentMaxWidth = 600.dp
            )
            ScreenSize.MEDIUM -> ResponsiveLayoutConfig(
                contentMaxWidth = 840.dp
            )
            ScreenSize.LARGE -> ResponsiveLayoutConfig(
                contentMaxWidth = 1200.dp
            )
            ScreenSize.EXTRA_LARGE -> ResponsiveLayoutConfig(
                contentMaxWidth = 1600.dp
            )
        }
    }
}

// Composable để cung cấp cấu hình responsive
@Composable
fun ResponsiveContent(content: @Composable (ResponsiveConfig, ResponsiveLayoutConfig) -> Unit) {
    val responsiveConfig = rememberResponsiveConfig()
    val layoutConfig = ResponsiveLayoutProvider.getLayoutConfig(responsiveConfig.screenSize)

    content(responsiveConfig, layoutConfig)
}