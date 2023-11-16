package com.example.yaming

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast


class CameraBtClickDialog(context: Context, private val listener: OnDataCameraEnteredListener) : Dialog(context) {
    interface OnDataCameraEnteredListener {
        fun onDataCameraEntered(meal: String)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.camera_bt_click_dialog)

        val btSaveDialog = findViewById<Button>(R.id.cameraBtMealSave)
        btSaveDialog.setOnClickListener {
            val camerabtradioBreakfast = findViewById<RadioButton>(R.id.cameraRadioBreakfast)
            val camerabtradioLunch = findViewById<RadioButton>(R.id.cameraRadioLunch)
            val camerabtradioDinner = findViewById<RadioButton>(R.id.cameraRadioDinner)

            var meal: String = ""
            if(camerabtradioBreakfast.isChecked()){
                meal = "아침"

            }
            else if(camerabtradioLunch.isChecked()){
                meal = "점심"
            }
            else if(camerabtradioDinner.isChecked()){
                meal = "저녁"
            }
            else{
                Toast.makeText(context, "식사를 선택하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            listener.onDataCameraEntered(meal)
            dismiss()
        }
    }
}