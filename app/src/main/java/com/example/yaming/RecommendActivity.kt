package com.example.yaming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class RecommendActivity : AppCompatActivity() {
    fun clickoneDayTotalCal(view: View){
        try{
            val receivedTodayTotalCal = intent.getDoubleExtra("TodayTotalCal", 0.0)
            val textView = findViewById<TextView>(R.id.oneDayTotalCal)
            textView.text = "${receivedTodayTotalCal}cal"
        }
        catch (e: Exception) {
            e.printStackTrace() // 예외 내용을 로그에 출력
            Toast.makeText(this, "값을 가져오는 중입니다.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)



    }
}