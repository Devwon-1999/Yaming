package com.example.yaming
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.*
import retrofit2.http.Query

data class Post(val email: String)
public interface ApiService {

    @POST("/login/user")
    fun getUser(@Query("email") email: String): Call<Post>
//    @Header("token") token: String?,
//    @POST("/createPost")
//    fun createPost(@Body post: Post): Call<Post>
}


class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private fun sendRequest() {
        val call = apiService.getUser(email = "user@example.com") // API 메소드 호출
        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    val post = response.body()
                    if (post != null) {
                        // 요청에 대한 응답을 처리
                        println("제목: ${post.email}")

                    } else {
                        println("데이터가 없습니다.")
                    }
                } else {
                    println("요청에 실패했습니다. HTTP 에러 코드: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
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

            sendRequest()
        }


        val btsignup = findViewById<View>(R.id.login_btsignup) //회원가입 버튼 클릭시 화면 전환
        btsignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}