package com.levi.resendmsg.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import com.levi.resendmsg.R
import com.levi.resendmsg.helper.SpHelper
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by levi on 2018/1/4.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */

class MainActivity : AppCompatActivity() {
    private lateinit var sp: SpHelper
    val TAG = "MainActivity"
    private val permissions = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = SpHelper(this)

        checkPermission()

        changeButtonText(sp.translate)
        editWrapper.hint = resources.getString(R.string.receive_number)
        targetNum.setText(sp.target, TextView.BufferType.EDITABLE)
        targetNum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeButtonText(false)
            }
        })
        switchBtn.setOnClickListener({ v -> switchState() })
    }

    private fun changeState(state: Boolean) {
        sp.translate = state
        changeButtonText(state)
    }

    private fun switchState() {
        if (targetNum.text.toString().equals("")) {
            Toast.makeText(this, resources.getString(R.string.hint), Toast.LENGTH_SHORT).show()
            sp.target = ""
            sp.translate = false
            changeButtonText(sp.translate)
            return
        }
        sp.target = targetNum.text.toString()
        sp.translate = !sp.translate
        changeButtonText(sp.translate)
    }

    private fun changeButtonText(state: Boolean) = if (state)
        switchBtn.text = resources.getString(R.string.translating)
    else
        switchBtn.text = resources.getString(R.string.startTranslate)

    private fun checkPermission() {
        requestPermission(permissions.
                filter {
                    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED
                }.
                toTypedArray()
        )
    }

    private fun requestPermission(permission: Array<String>) {
        if(permission.isEmpty())
            return

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission[0])) {
            Toast.makeText(this, R.string.requestPermissionInfo, Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(this, permission, 404)
        }
        else
            ActivityCompat.requestPermissions(this, permissions, 404)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, R.string.grantPermissionInfo, Toast.LENGTH_LONG).show()
            sp.translate = false
            this.finish()
        }
    }
}