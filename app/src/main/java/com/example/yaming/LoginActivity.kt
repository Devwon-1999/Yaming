package com.example.yaming
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val apiClient = ApiClient() //Api 클라 생성

    private fun fetchDataFromServer() { //데이터를 서버로 부터 가져오는 함수 구현 필요
        val call: Call<DataResponse> = apiClient.apiService.fetchData()

        call.enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    // 데이터 사용
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)//로그인 폼 시작


        val btsignin = findViewById<View>(R.id.login_btlogin)
        btsignin.setOnClickListener{
            val intent = Intent(this, MainUIActivity::class.java)
            startActivity(intent)

            fetchDataFromServer()
        }


        val btsignup = findViewById<View>(R.id.login_btsignup) //회원가입 버튼 클릭시 화면 전환
        btsignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}