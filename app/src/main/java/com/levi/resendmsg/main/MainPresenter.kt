package com.levi.resendmsg.main

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.levi.resendmsg.R
import com.levi.resendmsg.helper.SendMsgHelper
import com.levi.resendmsg.helper.SpHelper

/**
 * Created by levi on 2018/1/17.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */

class MainPresenter(private val view : MainContractor.IMainView) : MainContractor.IMainPresenter {
    val sp : SpHelper
    private val permissions = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS)

    init {
        view.presenter = this
        sp = SpHelper(view.context)
        checkPermission(permissions)
        view.changeButtonState(sp.state)
        view.changeText(sp.target)
        for ((index, sInfo) in SendMsgHelper.getSIMList(view.context)!!.withIndex()) {
            view.addSIM(sInfo.number, index)
        }
    }

    override fun changeSlotID(slotID: Int) {
        sp.slotID = slotID
    }

    override fun changeState(target: String) {
        sp.target = target
        if (target == "")
            sp.state = false
        else
            sp.state = !sp.state
        view.changeButtonState(sp.state)
    }

    override fun checkPermission(permission: Array<String>) {
        requestPermission(permissions.
                filter {
                    ContextCompat.checkSelfPermission(view.context, it) == PackageManager.PERMISSION_DENIED
                }.
                toTypedArray()
        )
    }

    override fun requestPermission(permission: Array<String>) {
        if(permission.isEmpty())
            return

        if (ActivityCompat.shouldShowRequestPermissionRationale(view.activity, permission[0])) {
            Toast.makeText(view.context, R.string.requestPermissionInfo, Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(view.activity, permission, 404)
        }
        else
            ActivityCompat.requestPermissions(view.activity, permissions, 404)
    }

    override fun requestPermissionResult(grantResults: IntArray) {
        if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(view.context, R.string.grantPermissionInfo, Toast.LENGTH_LONG).show()
            sp.state = false
            view.quit()
        }
    }
}

