package com.example.yaming

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast


class CustomDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.userinput_custom_dialog)

        val btSaveDialog = findViewById<Button>(R.id.btSaveDialog)
        btSaveDialog.setOnClickListener {
            val EditTextoneMealTotalCal = findViewById<EditText>(R.id.oneMealTotalCal)
            val oneMealTotalCal = EditTextoneMealTotalCal.text.toString()

            val EditTextoneMealTotalTan = findViewById<EditText>(R.id.oneMealTotalTan)
            val oneMealTotalTan = EditTextoneMealTotalTan.text.toString()

            val EditTextoneMealTotalDan = findViewById<EditText>(R.id.oneMealTotalDan)
            val oneMealTotalDan = EditTextoneMealTotalDan.text.toString()

            val EditTextoneMealTotalJi = findViewById<EditText>(R.id.oneMealTotalJi)
            val oneMealTotalJi = EditTextoneMealTotalJi.text.toString()

            val radioBreakfast = findViewById<RadioButton>(R.id.radioBreakfast)
            val radioLunch = findViewById<RadioButton>(R.id.radioLunch)
            val radioDinner = findViewById<RadioButton>(R.id.radioDinner)
            var meal: String = ""
            if(radioBreakfast.isChecked()){
                meal = "아침"
            }
            else if(radioLunch.isChecked()){
                meal = "점심"
            }
            else if(radioLunch.isChecked()){
                meal = "저녁"
            }
            else{
                Toast.makeText(context, "식사를 선택하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

        val btCloseDialog = findViewById<Button>(R.id.btCloseDialog)
        btCloseDialog.setOnClickListener {

            dismiss()
        }
    }
}