package com.example.testapi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.testapi.R
import com.example.testapi.data.model_component.SlideData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SlideComponentUniversal(
    modifier: Modifier = Modifier,
    slides: List<SlideData>
) {
    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    // Tự động chuyển slide
    LaunchedEffect(pagerState.currentPage, slides) {
        if (slides.isNotEmpty()) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % slides.size
            coroutineScope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(216.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (slides.isNotEmpty()) {
            if (slides.size > 1) {
                SlideImage(
                    slide = slides[(pagerState.currentPage - 1 + slides.size) % slides.size],
                    modifier = Modifier
                        .width(80.dp)
                        .height(120.dp)
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(8.dp))
                )
                SlideImage(
                    slide = slides[(pagerState.currentPage + 1) % slides.size],
                    modifier = Modifier
                        .width(80.dp)
                        .height(120.dp)
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    SlideImage(
                        slide = slides[page],
                        modifier = Modifier.fillMaxSize()
                    )
                }
                DotsIndicator(
                    total = slides.size,
                    current = pagerState.currentPage,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SlideImage(
    slide: SlideData,
    modifier: Modifier = Modifier
) {
    when (slide) {
        is SlideData.Local -> {
            Image(
                painter = painterResource(id = slide.resId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }

        is SlideData.Url -> {
            AsyncImage(
                model = slide.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
    }
}

@Composable
fun DotsIndicator(total: Int, current: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (index == current) Color.Gray else colorResource(R.color.black4))
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}