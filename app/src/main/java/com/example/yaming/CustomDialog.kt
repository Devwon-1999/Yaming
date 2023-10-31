package com.example.yaming

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button


class CustomDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.userinput_custom_dialog)

        // 여기에서 custom_dialog_layout.xml의 뷰 요소에 대한 작업을 수행하세요.
        val btCloseDialog = findViewById<Button>(R.id.btCloseDialog)
        btCloseDialog.setOnClickListener {
            // 다이얼로그를 닫기 위한 동작을 추가하세요.
            dismiss()
        }
    }
}