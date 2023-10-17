package com.example.yaming
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val apiClient = ApiClient() //Api 클라 생성

//    private fun fetchDataFromServer() { //데이터를 서버로 부터 가져오는 함수 구현 필요
//        val call: Call<DataResponse> = apiClient.apiService.fetchData()
//
//        call.enqueue(object : Callback<DataResponse> {
//            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    // 데이터 사용
//                } else {
//                    // 오류 처리
//                }
//            }
//
//            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
//                // 네트워크 오류 처리
//            }
//        })
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)//로그인 폼 시작


        val btsignin = findViewById<View>(R.id.login_btlogin)
        btsignin.setOnClickListener{
            val login_email = findViewById<EditText>(R.id.login_email)
            val login_password = findViewById<EditText>(R.id.login_password)
            val email = login_email.text.toString()
            val password = login_password.text.toString()

            login(email, password)

//            val intent = Intent(this, MainUIActivity::class.java)
//            startActivity(intent)

        }


        val btsignup = findViewById<View>(R.id.login_btsignup) //회원가입 버튼 클릭시 화면 전환
        btsignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
    private fun login(username: String, password: String) {
        val call: Call<LoginResponse> = apiClient.apiService.login(username, password)

        // 비동기적으로 서버로부터 응답을 받기 위해 enqueue를 사용
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) { // 서버 응답이 성공적으로 돌아왔을 때
                    val loginResponse = response.body()

                    if (loginResponse != null) {            // 토큰을 저장하고 성공적으로 로그인을 처리
                        val token: String = loginResponse.token     // 토큰을 SharedPreferences 등을 사용하여 저장
                        TokenManager.saveToken(this@LoginActivity, token)
                        runOnUiThread {
                            // 예시: 메인 화면으로 이동
                            val intent = Intent(this@LoginActivity, MainUIActivity::class.java)
                            startActivity(intent)
                            finish() // 로그인 화면을 종료
                        }
                    } else {// 서버 응답에 토큰이 없을 때 오류 처리
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "서버에서 토큰을 제대로 받지 못했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {// 서버 응답이 실패일 때 오류 처리 (예: 인증 실패)
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "로그인 실패. 아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // 네트워크 오류 처리 (예: 서버에 연결할 수 없음)
                if (t is IOException) {
                    // 네트워크 오류: 서버에 연결할 수 없음 또는 응답이 오래 걸림
                    // 예를 들어, 인터넷 연결이 끊어진 경우 등

                    // 네트워크 오류를 사용자에게 알리거나 적절한 조치를 취합니다.
                    // 이 예시에서는 Toast 메시지를 표시합니다.
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "네트워크 연결에 문제가 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 기타 오류: 네트워크 오류 이외의 오류 (예: 서버 응답이 예기치 않은 형식)

                    // 오류를 로깅하거나 사용자에게 적절한 오류 메시지를 표시합니다.
                    // 이 예시에서는 Logcat에 오류를 기록합니다.
                    Log.e("NetworkFailure", "네트워크 호출에서 오류 발생", t)

                    // 또는 사용자에게 오류 다이얼로그를 표시하거나 다른 오류 처리 방법을 선택할 수 있습니다.
                }
            }
        })
    }
}