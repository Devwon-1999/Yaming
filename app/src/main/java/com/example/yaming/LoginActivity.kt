package com.example.yaming
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


data class ResponseData(val data: Map<String, Any?> )
//데이터베이스에서 가져오기위한 데이터 클래스 생성
//Map형식 키 - 값

class UserRequest internal constructor(val email: String)
//email 속성을 갖는 UserRequest 클래스 생성

interface LoginService { //Retrofit 라이브러리를 사용하여 HTTP요청을 처리하기 위한 API 서비스 인터페이스 정의
    @POST("/user/getUser") //요청
    fun getUser(@Body body: UserRequest): Call<ResponseData>
}

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: LoginService
    private fun sendRequest(email: String, password: String) {
        val call = apiService.getUser(UserRequest(email))
        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {   //응답 확인 함수

//                if (response.isSuccessful) {                                                        //응답을 성공적으로 받았을 때
//                    println(response.body()!!.data["name"])                                         //데이터의 키값을 통해 불러오기
//                    println(response.body())
//                    if(response.body()!!.data.isEmpty()){                                           //응답 O 데이터 X시 *오류 발생 *예외처리 필수*
//                        println("가져온 데이터가 없습니다")
//                    }
//                    else if(response.body()!!.data["email"] == email && response.body()!!.data["password"] == password){    //응답 O 데이터 O, 이메일과 비밀번호 일치시
//                        Toast.makeText(this@LoginActivity, "${response.body()!!.data["name"]}님 환영합니다. \n 로그인이 성공적으로 완료되었습니다.", Toast.LENGTH_LONG).show()
//                        val intent = Intent(this@LoginActivity, MainUIActivity::class.java)
//                        startActivity(intent)
//                    }
//                    else if(response.body()!!.data["email"] != email){                                                   //응답 0 데이터 0, 이메일이 일치하지 않을 시
//                        Toast.makeText(this@LoginActivity, "존재하지 않는 이메일 입니다.", Toast.LENGTH_LONG).show()
//                    }
//                    else if(response.body()!!.data["email"] == email && response.body()!!.data["password"] != password){
//                        Toast.makeText(this@LoginActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
//                    }
//
//                }
//                else {                                                                              //응답을 받지 못했을 때
//                    println("요청에 실패했습니다. HTTP 에러 코드: ${response.code()}")
//                }

                if (response.isSuccessful) { //응답 O
                    val responseData = response.body()
                    if (responseData != null) { //응답 O 데이터 O
                        val data = responseData.data

                        try
                        {
                            if (data.isEmpty()) {// 응답 O 데이터 X 예외처리로 이동
                                println("가져온 데이터가 없습니다")
                            }
                            else {// 응답 O 데이터 O
                                if(response.body()!!.data["email"] == email && response.body()!!.data["password"] == password){
                                    Toast.makeText(this@LoginActivity, "${response.body()!!.data["name"]}님 환영합니다. \n 로그인이 성공적으로 완료되었습니다.", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@LoginActivity, MainUIActivity::class.java)
                                    startActivity(intent)
                                }
                                else if(response.body()!!.data["email"] == email && response.body()!!.data["password"] != password){
                                    Toast.makeText(this@LoginActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                                }
                                else if(response.body()!!.data["email"] != email){
                                    Toast.makeText(this@LoginActivity, "존재하지 않는 이메일 입니다.", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        catch (e: Exception)
                        {
                            // 예외가 발생한 경우의 처리
                            println("예외 발생: ${e.message}")
                            Toast.makeText(this@LoginActivity, "존재하지 않는 이메일입니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                    else{ //응답 O 데이터 X
                        // responseData가 null인 경우의 처리
                        println("응답 데이터가 null입니다.")
                    }
                }
                else{ //응답 X
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

        apiService = retrofit.create(LoginService::class.java)


        val btsignin = findViewById<View>(R.id.login_btlogin)
        btsignin.setOnClickListener{
            val login_email = findViewById<EditText>(R.id.login_email)
            val login_password = findViewById<EditText>(R.id.login_password)
            val email = login_email.text.toString()
            val password = login_password.text.toString()
            if(email.isEmpty()){
                Toast.makeText(this@LoginActivity, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else if(password.isEmpty()){
                Toast.makeText(this@LoginActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else{
                sendRequest(email, password)
            }
        }


        val btsignup = findViewById<View>(R.id.login_btsignup) //회원가입 버튼 클릭시 화면 전환
        btsignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}