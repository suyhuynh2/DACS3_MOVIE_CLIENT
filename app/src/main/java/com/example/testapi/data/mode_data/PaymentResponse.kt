package com.example.testapi.data.mode_data

data class PaymentResponse(
    val code: String,
    val desc: String,
    val data: PaymentData?
)

data class PaymentData(
    val checkoutUrl: String,
    val qrCode: String
)

data class PaymentRequest(
    val firebase_uid: String
)