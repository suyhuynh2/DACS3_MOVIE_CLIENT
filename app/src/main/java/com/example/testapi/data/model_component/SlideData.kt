package com.example.testapi.data.model_component

sealed class SlideData {
    data class Local(val resId: Int) : SlideData()
    data class Url(val imageUrl: String) : SlideData()
}