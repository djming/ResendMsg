package com.levi.resendmsg

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
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

        Toast.makeText(this, ""+ SendMsgHelper.getSIMInfo(this, 1), Toast.LENGTH_LONG).show()

        changeButtonText(sp.translate)
        editWrapper.hint = targetNum.hint
        targetNum.setText(sp.target, TextView.BufferType.EDITABLE)
        switchBtn.setOnClickListener({ v -> switchState() })
    }

    private fun switchState() {
        if (targetNum.text.toString().equals("")) {
            Toast.makeText(this, resources.getString(R.string.hint), Toast.LENGTH_SHORT).show()
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
