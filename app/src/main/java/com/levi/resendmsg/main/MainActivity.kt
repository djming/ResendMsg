package com.levi.resendmsg.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioButton
import android.widget.TextView
import com.levi.resendmsg.R
import com.levi.resendmsg.helper.SendMsgHelper
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by levi on 2018/1/4.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */

class MainActivity : AppCompatActivity(), MainContractor.IMainView {
    override lateinit var presenter: MainContractor.IMainPresenter
    override val context: Context = this
    override val activity: Activity = this

    override fun changeText(text: String) {
        targetNum.setText(text, TextView.BufferType.EDITABLE)
    }

    override fun changeButtonState(state: Boolean) = if (state)
        switchBtn.text = resources.getString(R.string.translating)
    else
        switchBtn.text = resources.getString(R.string.startTranslate)

    override fun quit() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        editWrapper.hint = resources.getString(R.string.receive_number)
        targetNum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.changeState()
            }
        })
        switchBtn.setOnClickListener({ _ -> presenter.changeState(targetNum.text.toString()) })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.requestPermissionResult(grantResults)
    }

    override fun addSIM(num: String, slotID : Int) {
        val radioBtn = RadioButton(this)
        radioBtn.text = if(num.startsWith("+")) num.subSequence(3, num.length) else num
        radioBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                presenter.changeSlotID(slotID)
        }
        simList.addView(radioBtn)
    }

    override fun showNotification() {
        var notification = NotificationCompat.Builder(this)
    }
}
