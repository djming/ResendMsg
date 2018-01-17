package com.levi.resendmsg.main

import android.app.Activity
import android.content.Context
import com.levi.resendmsg.IBasePresenter
import com.levi.resendmsg.IBaseView

/**
 * Created by levi on 2018/1/6.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */
 
class MainContractor {
    interface IMainPresenter : IBasePresenter {
        fun changeState(target : String = "")
        fun checkPermission(permission : Array<String>)
        fun requestPermission(permission : Array<String>)
        fun requestPermissionResult(grantResults: IntArray)
    }

    interface IMainView : IBaseView<IMainPresenter> {
        val context : Context
        val activity : Activity
        fun quit()
        fun changeText(text : String)
        fun changeButtonState(state : Boolean)
    }
}