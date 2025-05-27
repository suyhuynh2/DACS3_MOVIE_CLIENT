package com.example.testapi.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.testapi.R
import com.example.testapi.data.model_component.BottomMenuItem
import com.example.testapi.data.model_component.Screen

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val bottomMenuItemsList = prepareButtomMenu()

    BottomAppBar(
        cutoutShape = CircleShape,
        contentColor = colorResource(id = R.color.white),
        backgroundColor = colorResource(id = R.color.black2),
        elevation = 3.dp
    ) {
        bottomMenuItemsList.forEach { bottomMenuItem ->
            val screen = when (bottomMenuItem.label) {
                "Home" -> Screen.Home
                "Tôi" -> Screen.Profile
                "Kho phim" -> Screen.MenuMovie
                else -> Screen.Home
            }

            BottomNavigationItem(
                selected = (currentScreen == screen),
                onClick = {
                    onScreenSelected(screen)
                },
                icon = {
                    Icon(
                        painter = bottomMenuItem.icon,
                        contentDescription = bottomMenuItem.label,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                },
                label = {
                    Text(
                        text = bottomMenuItem.label,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                },
                selectedContentColor = colorResource(id = R.color.white),
                unselectedContentColor = colorResource(id = R.color.gray),
                alwaysShowLabel = true,
                enabled = true
            )

        }
    }
}

@Composable
fun prepareButtomMenu(): List<BottomMenuItem> {
    return listOf(
        BottomMenuItem(
            label = "Trang chủ",
            icon = painterResource(id = R.drawable.btn_1)
        ),
        BottomMenuItem(
            label = "Kho phim",
            icon = painterResource(id = R.drawable.grid_view)
        ),
        BottomMenuItem(
            label = "Tôi",
            icon = painterResource(id = R.drawable.btn_4)
        )
    )
}