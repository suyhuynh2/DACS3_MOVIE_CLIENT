package com.example.testapi.ui.screens.payment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.Color as ComposeColor
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapi.data.repository.PaymentRepository
import com.example.testapi.ui.components.SystemUIWrapper
import com.example.testapi.viewmodel.PaymentViewModel
import com.example.testapi.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemUIWrapper {
                MaterialTheme(colors = darkColors()) {
                    Surface(color = MaterialTheme.colors.background) {
                        PaymentScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentScreen() {
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.userLiveData.observeAsState()
    val context = LocalContext.current
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val firebaseUid = firebaseUser?.uid ?: return

    val viewModel = remember { PaymentViewModel(PaymentRepository()) }

    val paymentData by viewModel.paymentData.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showSuccessMessage by remember { mutableStateOf(false) }
    var previousRole by remember { mutableStateOf<String?>(null) }

    // Gọi API khi khởi tạo màn hình
    LaunchedEffect(Unit) {
        viewModel.createPayment(firebaseUid)
    }

    // Kết nối Pusher khi vào màn hình
    LaunchedEffect(Unit) {
        userViewModel.setupPusherConnection(context, firebaseUid)
    }

    // Theo dõi sự thay đổi role
    LaunchedEffect(user?.role) {
        val currentRole = user?.role
        if (previousRole == "FREE" && currentRole == "VIP") {
            showSuccessMessage = true
        }
        previousRole = currentRole
    }

    // Giao diện chính
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Nâng cấp tài khoản",
            color = MaterialTheme.colors.onBackground,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (user?.role) {
            "VIP" -> {
                if (showSuccessMessage) {
                    Text(
                        text = "🎉 Thanh toán thành công!",
                        color = ComposeColor(0xFF4CAF50),
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = "Cảm ơn bạn đã trở thành VIP của tôi 💖",
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                Text(
                    text = "Chỉ cần 2000 VNĐ để nâng cấp tài khoản thành tài khoản VIP",
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                when {
                    loading -> CircularProgressIndicator(color = MaterialTheme.colors.primary)

                    paymentData != null -> {
                        val qrBitmap = remember(paymentData) {
                            generateQrCodeBitmap(paymentData!!.qrCode)
                        }

                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(250.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    error != null -> {
                        Text(
                            text = error ?: "Đã có lỗi xảy ra",
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
    }
}

fun generateQrCodeBitmap(data: String): Bitmap {
    val size = 512
    val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
        }
    }
    return bmp
}
