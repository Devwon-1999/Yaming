package com.example.yaming
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.ByteArrayOutputStream
import java.util.Timer
import java.util.TimerTask

interface ApiService {
    @Multipart
    @POST("/model/predict")
    fun  uploadPhoto(@Part image: MultipartBody.Part?): Call<UploadResponse>
}

data class UploadResponse(
    @SerializedName("backbone") val backbone: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("class") val classValue: Int,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("food_info") val foodInfo: List<List<Any>> // 또는 Array<String>으로 변경할 수 있음
)

class MainUIActivity : AppCompatActivity(), UserInputDialog.OnDataEnteredListener, CameraBtClickDialog.OnDataCameraEnteredListener {
    private val CAMERA_REQUEST_CODE = 101
    private val MAX_PERMISSION_REQUESTS = 3 // 최대 요청 횟수
    private val PERMISSION_REQUEST_INTERVAL = 5000L // 권한 요청 간격 (밀리초)
    private var permissionRequests = 0
    private var permissionRequestTimer: Timer? = null

    private var totalTan:Double = 0.0
    private var totalDan:Double = 0.0
    private var totalJi:Double = 0.0

    private var cameraCal:Double = 0.0
    private var cameraTan:Double = 0.0
    private var cameraDan:Double = 0.0
    private var cameraJi:Double = 0.0
    override fun onDataEntered(oneMealTotalCal: String, oneMealTotalTan: String, oneMealTotalDan: String, oneMealTotalJi: String, meal: String) {
        //직접입력에서 데이터가 넘어온 경우의 후처리
        val oneMealTotalCal = oneMealTotalCal.toDouble()
        val oneMealTotalTan = oneMealTotalTan.toDouble()
        val oneMealTotalDan = oneMealTotalDan.toDouble()
        val oneMealTotalJi = oneMealTotalJi.toDouble()

        val showTodayTan = findViewById<TextView>(R.id.todayTan)
        val showTodayDan = findViewById<TextView>(R.id.todayDan)
        val showTodayJi = findViewById<TextView>(R.id.todayJi)


        totalTan += oneMealTotalTan
        totalDan += oneMealTotalDan
        totalJi += oneMealTotalJi

        showTodayTan.text = "${totalTan}g"
        showTodayDan.text = "${totalDan}g"
        showTodayJi.text = "${totalJi}g"
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

    override fun onDataCameraEntered(meal: String){
        val showTodayTan = findViewById<TextView>(R.id.todayTan)
        val showTodayDan = findViewById<TextView>(R.id.todayDan)
        val showTodayJi = findViewById<TextView>(R.id.todayJi)

        cameraTan += cameraTan
        cameraDan += cameraDan
        cameraJi += cameraJi

        showTodayTan.text = "${cameraTan}g"
        showTodayDan.text = "${cameraDan}g"
        showTodayJi.text = "${cameraJi}g"

        if(meal == "아침"){
            val showBreakfastCal = findViewById<TextView>(R.id.breakfastCal)
            showBreakfastCal.text = "${cameraCal}Cal"
        }
        else if(meal == "점심"){
            val showLunchCal = findViewById<TextView>(R.id.lunchCal)
            showLunchCal.text = "${cameraCal}Cal"
        }
        else if(meal == "저녁"){
            val showDinnerCal = findViewById<TextView>(R.id.dinnerCal)
            showDinnerCal.text = "${cameraCal}Cal"
        }
        else{
            Toast.makeText(this, "데이터가 정상적으로 전달되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun clickdailyAmount(view: View){
        try {
            val intent = intent
            val value = intent.getStringExtra("weight")
            val textView = findViewById<TextView>(R.id.dailyAmount)
            val context = applicationContext
            if (value!!.isNotEmpty()) {
                val intvalue = value.toDouble().toInt()
                textView.text = "${intvalue * 30}"
            } else {
                textView.text = "데이터 없음"
            }
        } catch (e: Exception) {
            e.printStackTrace() // 예외 내용을 로그에 출력
            Toast.makeText(this, "값을 가져오는 중입니다.", Toast.LENGTH_SHORT).show()
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
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            val requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes)
            val photoPart = MultipartBody.Part.createFormData("image", "image.jpeg", requestFile)

            val retrofit = Retrofit.Builder()
                .baseUrl("http://35.230.1.210:8000") // 실제 서버 주소에 맞게 수정하세요
                .addConverterFactory(GsonConverterFactory.create())
                .build()


            val apiService: ApiService = retrofit.create(ApiService::class.java)
            val call: Call<UploadResponse> = apiService.uploadPhoto(photoPart)
            call.enqueue(object : Callback<UploadResponse> {

                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    // 성공적으로 업로드됐을 때의 처리
                    if (response.isSuccessful) {
                        val uploadResponse: UploadResponse? = response.body()
                        if (uploadResponse != null) {
                            // 서버 응답을 처리
                            val backbone = uploadResponse.backbone
                            val success = uploadResponse.success
                            val classValue = uploadResponse.classValue
                            val foodName = uploadResponse.foodName
                            val foodInfo = uploadResponse.foodInfo
                            cameraCal += foodInfo[0][2] as Double
                            cameraTan += foodInfo[0][3] as Double
                            cameraDan += foodInfo[0][5] as Double
                            cameraJi += foodInfo[0][4] as Double



                            Log.e("ji", "$cameraJi")
                            Log.e("dan", "$cameraDan")
                            Log.e("tan", "$cameraTan")
                            Log.e("cal", "$cameraCal")

                        }
                    } else {
                        // 서버 응답이 실패한 경우
                        // 예: val errorCode = response.code()
                        // val errorMessage = response.message()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    // 업로드 실패 시의 처리
                    t.printStackTrace()
                    Log.e("TAG", "여기6")
                }
            })
        }
        val customDialog = CameraBtClickDialog(this, this)
        customDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionRequestTimer?.cancel() // 액티비티가 종료되면 타이머 취소
    }
}

