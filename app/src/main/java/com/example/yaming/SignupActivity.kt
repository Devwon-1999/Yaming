package com.example.yaming

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import java.util.regex.Pattern


class SignupActivity : AppCompatActivity() {
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // 정규 표현식을 사용하여 전화번호 형식을 확인
        val pattern = "^[2-9][0-9]{2}-[2-9][0-9]{2}-[0-9]{4}\$"
        return phoneNumber.matches(pattern.toRegex())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btsignin = findViewById<View>(R.id.btsignin) //가입버튼 클릭시 호출
        btsignin.setOnClickListener{

            val editTextname = findViewById<EditText>(R.id.editTextName) //이름
            val name = editTextname.text.toString()


            val editTextemail = findViewById<EditText>(R.id.editTextEmail) //이메일
            val email = editTextemail.text.toString()

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"       //이메일 형식확인
            editTextemail.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val email = editTextemail.text.toString()
                    if (!Pattern.matches(emailPattern, email)) {
                        editTextemail.error = "올바른 이메일 형식이 아닙니다."
                    }
                }
            }


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
            }

            val editTextheight = findViewById<EditText>(R.id.editTextHeight) //키
            val height = editTextheight.text.toString()


            val editTextweight = findViewById<EditText>(R.id.editTextWeight) //몸무게
            val weight = editTextweight.text.toString()

//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
        }

//        fun postlogin(name: String, email: String, password: String, phonenum: String, age: Int, sex :String, height :Int, weight :Int){
//
//        }


        val btback = findViewById<View>(R.id.btback) //뒤로가기
        btback.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}