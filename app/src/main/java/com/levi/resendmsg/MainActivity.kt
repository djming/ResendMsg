package com.levi.resendmsg

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val sp = SpHelper(applicationContext)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var target = sp.target
        var translate = sp.translate

        if (translate)
            switchBtn.text = "转发中"
        else
            switchBtn.text = "开启转发"

        editWrapper.hint = targetNum.hint

        switchBtn.setOnClickListener({ v -> switchState()})
    }

    fun switchState() {
        sp.translate = !sp.translate
        val state = sp.translate
    }
}
