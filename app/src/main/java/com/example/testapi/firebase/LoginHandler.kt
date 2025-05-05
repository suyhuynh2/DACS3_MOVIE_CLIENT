package com.example.testapi.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.testapi.data.repository.UsersRepository
import com.example.testapi.ui.screens.screen.MainScreenActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginHandler(private val context: Context) {

    private val usersRepository = UsersRepository()

    fun handleLogin(user: FirebaseUser) {
        val userRequest = UserFactory.fromFirebaseUser(user)

        CoroutineScope(Dispatchers.Main).launch {
            val result = usersRepository.createUser(userRequest)

            result.onSuccess { userResponse ->
                val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("user_id", userResponse.firebase_uid)
                    putString("user_name", userResponse.name)
                    putString("user_email", user.email)
                    putString("user_role", userResponse.role)
                    putString("user_provider", userResponse.provider)
                    apply()
                }

                val name = userResponse.name
                Toast.makeText(context, "Xin chào $name!", Toast.LENGTH_SHORT).show()

                val intent = Intent(context, MainScreenActivity::class.java).apply {
                    putExtra("userName", name)
                }
                context.startActivity(intent)
                if (context is Activity) context.finish()
            }.onFailure { error ->
                Toast.makeText(context, "Lỗi: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}