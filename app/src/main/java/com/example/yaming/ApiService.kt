package com.example.yaming

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService { //Retrofit을 사용하여 서버와 통신하는 데 필요한 API 인터페이스를 생성
    @GET("/api/data") // 서버의 엔드포인트에 따라 수정
    fun fetchData(): Call<DataResponse>

    @FormUrlEncoded
    @POST("/api/login") // 로그인 엔드포인트에 따라 수정
    fun login(@Field("emain") email: String, @Field("password") password: String): Call<LoginResponse>
}