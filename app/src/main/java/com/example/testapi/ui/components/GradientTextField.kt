package com.example.testapi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapi.R

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GradientTextFieldPreview() {
    GradientTextField(
        value = "",
        onValueChange = {},
        hint = "Nhập gì đó"
    )
}

@Composable
fun GradientTextField(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.pink),
                        colorResource(R.color.green)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(1.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = colorResource(R.color.black1),
                    shape = RoundedCornerShape(10.dp)
                )
                .align(Alignment.Center),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart // Hint sẽ căn giữa dọc
                ) {
                    if (value.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}