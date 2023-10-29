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
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask

//카메라 관련 수정 필요

class MainUIActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 101
    private val MAX_PERMISSION_REQUESTS = 3 // 최대 요청 횟수
    private val PERMISSION_REQUEST_INTERVAL = 5000L // 권한 요청 간격 (밀리초)

    private var permissionRequests = 0
    private var permissionRequestTimer: Timer? = null


//    val breakfastCal = 0
//    val lunchCal = 0
//    val dinnerCal = 0
//    val weight = 70
//    val dailyAmount = weight * 30

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

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            try {
                // 선택한 날짜에 대한 작업을 수행합니다.
                // 이곳에서 날짜의 배경색을 변경할 수 있습니다.
                // 예를 들어, 선택한 날짜의 배경색을 빨간색으로 변경하려면 다음과 같이 하세요.

                // 날짜 선택 시 기본 색상을 제거하려면 선택한 날짜 이외의 날짜를 초기화합니다.
                calendarView.date = calendarView.date // 이 줄은 선택한 날짜만 강조 표시하도록 합니다.

                // 선택한 날짜의 배경색 변경
                val selectedDateView = (calendarView.getChildAt(0) as ViewGroup).getChildAt(dayOfMonth - 1)
                selectedDateView.setBackgroundResource(R.drawable.selected_date_background) // 배경 리소스를 사용해 배경색을 변경할 수 있습니다.
            } catch (e: Exception) {
                e.printStackTrace()
                // 예외가 발생한 경우 여기서 처리할 코드를 추가할 수 있습니다.
            }
        }

        val btCamera = findViewById<View>(R.id.btCameraOpen) //카메라의 버튼이 눌렸을 때
        btCamera.setOnClickListener{
            requestCameraPermission()
        }
        val btUserInput = findViewById<View>(R.id.btUserInput)
        btUserInput.setOnClickListener {

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

                // 일정 간격으로 권한 요청을 반복
                permissionRequestTimer = Timer()
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



    override fun onDestroy() {
        super.onDestroy()
        permissionRequestTimer?.cancel() // 액티비티가 종료되면 타이머 취소
    }

}

