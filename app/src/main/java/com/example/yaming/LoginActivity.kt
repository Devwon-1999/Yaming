package com.example.yaming
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


data class ResponseData(val data: Map<String, Any?> )

class UserRequest internal constructor(val email: String)

interface ApiService {
    @POST("/user/getUser")
    fun getUser(@Body body: UserRequest): Call<ResponseData>
}

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private fun sendRequest(email: String) {
        val call = apiService.getUser(UserRequest(email)) // API 메소드 호출
        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {
                    println(response.body()!!.data["name"])
                    if(response.body()!!.data.isEmpty()){
                        println("가져온 데이터가 없습니다")
                    }
                    else{
                        println("로그인 성공")
                    }

                }
                else {
                    println("요청에 실패했습니다. HTTP 에러 코드: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                println("네트워크 오류: ${t.message}")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)//로그인 폼 시작

        var retrofit = Retrofit.Builder()
            .baseUrl("https://yaming-server.vercel.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)


        val btsignin = findViewById<View>(R.id.login_btlogin)
        btsignin.setOnClickListener{
            val login_email = findViewById<EditText>(R.id.login_email)
            val login_password = findViewById<EditText>(R.id.login_password)
            val email = login_email.text.toString()
            val password = login_password.text.toString()

            sendRequest(email)
        }


        val btsignup = findViewById<View>(R.id.login_btsignup) //회원가입 버튼 클릭시 화면 전환
        btsignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}