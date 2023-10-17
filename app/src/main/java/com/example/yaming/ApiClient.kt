package com.example.yaming

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient { //Retrofit을 사용하여 서버와 통신하는 Retrofit 클라이언트 클래스를 생성
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://localhost:3000") // 서버 URL에 따라 수정
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}