package com.levi.resendmsg.helper

import android.content.Context
import android.util.Log

/**
 * Created by levi on 2018/1/4.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */
 
class SpHelper(ctx : Context) {
    val TAG = "SpHelper"
    private val defaultTarget = ""

    private val sp by lazy {
        ctx.getSharedPreferences("state", Context.MODE_PRIVATE)
    }

    var target = sp.getString("target", defaultTarget)!!
        set(value) {
            if (value != field) {
                sp.edit().putString("target", value).apply()
                field = value
            }
        }

    var state = sp.getBoolean("state", false)
        set(value) {
            if (value != field) {
                sp.edit().putBoolean("state", value).apply()
                field = value
            }
        }
}