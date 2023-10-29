package com.example.yaming

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.regex.Pattern
import kotlin.collections.HashMap

data class SignupData(val success : Boolean)
class SignupRequest internal constructor(val data: Map<String, Any?>)

interface SignupService{
    @POST("/user/insertUser")
    fun insertUser(@Body body: SignupRequest): Call<SignupData>
}

class SignupActivity : AppCompatActivity() {
    private lateinit var apiService: SignupService
    private fun sendRequest(name: String, email: String, password: String, phoneNumber: String, age:Int, sex:String, height:Int, weight:Int) {
        val map : HashMap<String, Any> = HashMap()
        map["name"] = name
        map["email"] = email
        map["password"] = password
        map["phone"] = phoneNumber
        map["age"] = age
        map["sex"] = sex
        map["height"] = height
        map["weight"] = weight


        val call = apiService.insertUser(SignupRequest(map))
        call.enqueue(object : Callback<SignupData> {
            override fun onResponse(call: Call<SignupData>, response: Response<SignupData>) {

                if (response.isSuccessful) { //응답 O
                    val responseData = response.body()
                    if (responseData?.success == true) {
                        Toast.makeText(this@SignupActivity, "회원가입이 정상적으로 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)

                    }
                    else{
                        Toast.makeText(this@SignupActivity, "데이터 가다 말았다", Toast.LENGTH_SHORT).show()
                    }

                }
                else{ //응답 X
                    println("요청에 실패했습니다. HTTP 에러 코드: ${response.code()}")
                }

            }

            override fun onFailure(call: Call<SignupData>, t: Throwable) {
                println("네트워크 오류: ${t.message}")

            }
        })
    }
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // 정규 표현식을 사용하여 전화번호 형식을 확인
        val pattern = "^[0-9]{3}-[0-9]{4}-[0-9]{4}\$"
        return phoneNumber.matches(pattern.toRegex())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var retrofit = Retrofit.Builder()
            .baseUrl("https://yaming-server.vercel.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(SignupService::class.java)

        val btsignin = findViewById<View>(R.id.btsignin) //가입버튼 클릭시 호출
        btsignin.setOnClickListener{

            val editTextname = findViewById<EditText>(R.id.editTextName) //이름
            val name = editTextname.text.toString()

            val editTextemail = findViewById<EditText>(R.id.editTextEmail) //이메일
            val email = editTextemail.text.toString()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"       //이메일 형식확인
//            editTextemail.setOnFocusChangeListener { _, hasFocus ->
//                if (!hasFocus) {
//                    val email = editTextemail.text.toString()
//                    if (!Pattern.matches(emailPattern, email)) {
//                        editTextemail.error = "올바른 이메일 형식이 아닙니다."
//                    }
//                }
//            }


            val editTextpassword = findViewById<EditText>(R.id.editTextPassword) //비밀번호
            val password = editTextpassword.text.toString()

            val editTextpasswordcheck = findViewById<EditText>(R.id.editTextConfirmPassword) //비밀번호확인
            val passwordcheck = editTextpasswordcheck.text.toString()

//            editTextpasswordcheck.setOnFocusChangeListener { _, hasFocus ->       //비밀번호 체크 다른 로직으로
//                if (!hasFocus) {
//                    if (password != passwordcheck) {
//                        editTextpasswordcheck.error = "비밀번호가 일치하지 않습니다."
//                    }
//                }
//            }


            val editTextpn = findViewById<EditText>(R.id.editTextPhoneNumber) //전화번호
            val phonenum = editTextpn.text.toString()
//            if(isValidPhoneNumber(phonenum) != true){
//                editTextpn.error = "올바른 전화번호 형식이 아닙니다."
//            }


            val editTextage = findViewById<EditText>(R.id.editTextAge) //나이
            val age = editTextage.text.toString()


            val btnMale = findViewById<RadioButton> (R.id.radioButtonMale);
            val btnFemale = findViewById<RadioButton> (R.id.radioButtonFemale);

            var sex: String = ""
            if(btnMale.isChecked()){
                sex = "남자"
            }
            else if(btnFemale.isChecked()){
                sex = "여자"
            }else{
                Toast.makeText(this, "남자 또는 여자를 선택하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editTextheight = findViewById<EditText>(R.id.editTextHeight) //키
            val height = editTextheight.text.toString()


            val editTextweight = findViewById<EditText>(R.id.editTextWeight) //몸무게
            val weight = editTextweight.text.toString()



            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordcheck.isEmpty() ||
                phonenum.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty()) {
                Toast.makeText(this, "모든 입력값은 필수 입력값입니다.", Toast.LENGTH_SHORT).show()
            } else if (!Pattern.matches(emailPattern, email)) {
                editTextemail.error = "올바른 이메일 형식이 아닙니다."
            } else if (password != passwordcheck) {
                editTextpasswordcheck.error = "비밀번호가 일치하지 않습니다."
            } else if (!isValidPhoneNumber(phonenum)) {
                editTextpn.error = "올바른 전화번호 형식이 아닙니다."
            } else {
                val Iage = age.toInt()
                val Iheight = height.toInt()
                val Iweight = weight.toInt()
                
                //내장 DB에도 따로 저장
                val newUserInfo = UserInfoDB()
                newUserInfo.id = 1
                newUserInfo.height = Iheight
                newUserInfo.weight = Iweight
                val context = this
                val dataSource = WeightDataSource(context)
                val insertId = dataSource.addSource(newUserInfo)
                
                sendRequest(name, email, password, phonenum, Iage, sex, Iheight, Iweight)
            }
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
        }



        val btback = findViewById<View>(R.id.btback) //뒤로가기
        btback.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}