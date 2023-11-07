package com.example.yaming

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.widget.TextView
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask

class MainUIActivity : AppCompatActivity(), UserInputDialog.OnDataEnteredListener {
    private val CAMERA_REQUEST_CODE = 101
    private val MAX_PERMISSION_REQUESTS = 3 // 최대 요청 횟수
    private val PERMISSION_REQUEST_INTERVAL = 5000L // 권한 요청 간격 (밀리초)
    private var permissionRequests = 0
    private var permissionRequestTimer: Timer? = null

    override fun onDataEntered(oneMealTotalCal: String, oneMealTotalTan: String, oneMealTotalDan: String, oneMealTotalJi: String, meal: String) {
        // 이제 oneMealTotalCal, oneMealTotalTan, oneMealTotalDan, oneMealTotalJi, meal을 사용하여 원하는 작업을 수행할 수 있습니다.
        // 예를 들어, 이 데이터를 TextView에 표시하거나 데이터베이스에 저장할 수 있습니다.

        //테이블 형식으로 수정 필요
        val oneMealTotalCal = oneMealTotalCal.toInt()
        val oneMealTotalTan = oneMealTotalTan.toInt()
        val oneMealTotalDan = oneMealTotalDan.toInt()
        val oneMealTotalJi = oneMealTotalJi.toInt()

        val showTodayTan = findViewById<TextView>(R.id.todayTan)
        val showTodayDan = findViewById<TextView>(R.id.todayDan)
        val showTodayJi = findViewById<TextView>(R.id.todayJi)

        showTodayTan.text = "${oneMealTotalTan}g"
        showTodayDan.text = "${oneMealTotalDan}g"
        showTodayJi.text = "${oneMealTotalJi}g"
        if(meal == "아침"){
            val showBreakfastCal = findViewById<TextView>(R.id.breakfastCal)
            showBreakfastCal.text = "${oneMealTotalCal}Cal"


        }
        else if(meal == "점심"){
            val showLunchCal = findViewById<TextView>(R.id.lunchCal)
            showLunchCal.text = "${oneMealTotalCal}Cal"
        }
        else if(meal == "저녁"){
            val showDinnerCal = findViewById<TextView>(R.id.dinnerCal)
            showDinnerCal.text = "${oneMealTotalCal}Cal"
        }
        else{
            Toast.makeText(this, "데이터가 정상적으로 전달되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    fun clickdailyAmount(view: View){
        try {
            val textView = findViewById<TextView>(R.id.dailyAmount)
            val context = applicationContext
            val dataSource = WeightDataSource(context)
            val dataList = dataSource.getAllSource()

            if (dataList.isNotEmpty()) {
                val firstItem = dataList[0]
                textView.text = "${firstItem.weight * 30}"
            } else {
                textView.text = "데이터 없음"
            }
        } catch (e: Exception) {
            // 예외 처리: 데이터베이스 작업 중 예외 발생 시 실행할 코드
            e.printStackTrace() // 예외 내용을 로그에 출력
            Toast.makeText(this, "데이터베이스 오류", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_uiactivity)

        val btCamera = findViewById<View>(R.id.btCameraOpen) //카메라의 버튼이 눌렸을 때
        btCamera.setOnClickListener{
            requestCameraPermission()
        }

        val btUserInput = findViewById<View>(R.id.btUserInput) // 연필버튼이 눌렸을 때
        btUserInput.setOnClickListener {
            val customDialog = UserInputDialog(this, this)
            customDialog.show()
        }
        val btRecommend = findViewById<View>(R.id.btRecommend)
        btRecommend.setOnClickListener {
            val intent = Intent(this, RecommendActivity::class.java)
            startActivity(intent)
        }
        val btUser = findViewById<View>(R.id.btUser) // 하단 우측버튼눌렸을때
        btUser.setOnClickListener{
            val intent = Intent(this, UserDataActivity::class.java) //로그인 화면으로 전환
            startActivity(intent)
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestCameraPermission() {
        if (hasCameraPermission()) {
            // 권한이 이미 부여되었을 경우 처리
            openCamera()
        } else {
            if (permissionRequests < MAX_PERMISSION_REQUESTS) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
                permissionRequests++
                permissionRequestTimer = Timer() // 일정 간격으로 권한 요청을 반복
                permissionRequestTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        requestCameraPermission()
                    }
                }, PERMISSION_REQUEST_INTERVAL)
            } else {
                // 최대 요청 횟수를 초과한 경우 처리
                val message = "권한 요청 최대 횟수를 초과했습니다."
                val duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                Toast.makeText(this, message, duration).show()
                finish()
            }
        }
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
                finish()
            } else {
                // 사용자가 권한을 거부한 경우 또는 다른 처리를 추가
                val message = "권한을 거부할 시 앱을 사용할 수 없어요.\n 사용을 원할경우 설정 -> 권한 -> 카메라 권한 설정을 해주세요."
                val duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                Toast.makeText(this, message, duration).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 카메라로 촬영한 사진을 가져오는 코드
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // 여기에서 `imageBitmap`을 사용하거나 저장할 수 있습니다.
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionRequestTimer?.cancel() // 액티비티가 종료되면 타이머 취소
    }
}

