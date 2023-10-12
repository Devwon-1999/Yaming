package com.example.yaming

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)//로그인 폼 시작


        val btsignin = findViewById<View>(R.id.login_btlogin)
        btsignin.setOnClickListener{
            val intent = Intent(this, MainUIActivity::class.java)
            startActivity(intent)
        }


        val btsignup = findViewById<View>(R.id.login_btsignup) //회원가입 버튼 클릭시 화면 전환
        btsignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}