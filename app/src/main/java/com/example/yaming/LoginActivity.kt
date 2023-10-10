package com.example.yaming

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)

        val btsignup = findViewById<View>(R.id.btsignup) //회원가입 버튼 클릭시 화면 전환
        btsignup.setOnClickListener{
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }
}