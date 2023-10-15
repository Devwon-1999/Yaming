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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_uiactivity)

        val btcamera = findViewById<View>(R.id.btcamera) //카메라의 버튼이 눌렸을 때
        btcamera.setOnClickListener{
            requestCameraPermission()
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

