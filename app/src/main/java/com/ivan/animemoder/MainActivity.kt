package com.ivan.animemoder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // ПАРОЛЬ ДЛЯ ВХОДА (МОЖЕШЬ ПОМЕНЯТЬ ЗДЕСЬ)
    private val SECRET_PASSWORD = "IvanAdmin2024" 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val input = etPassword.text.toString()
            if (input == SECRET_PASSWORD) {
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
