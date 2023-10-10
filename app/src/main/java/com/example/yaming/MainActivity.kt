package com.example.yaming

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { //앱 최초 실행시 실행되는 함수
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //최초 화면 연결
    }

    override fun onTouchEvent(event: MotionEvent): Boolean { //화면 터치시 실행되는 함수
        if(event.action == MotionEvent.ACTION_DOWN){
           val intent = Intent(this, LoginActivity::class.java) //로그인 화면으로 전환
           startActivity(intent)
           return true
        }
        return super.onTouchEvent(event)
    }

}