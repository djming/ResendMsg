package com.levi.resendmsg

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
    private val defaultTarget = "15527163373"

    private val sp by lazy {
        ctx.getSharedPreferences("state", Context.MODE_PRIVATE)
    }

    var target = sp.getString("target", defaultTarget)
        set(value) {
            if (!value.equals(field)) {
                sp.edit().putString("target", value).apply()
                field = value
            }
        }

    var translate = sp.getBoolean("state", false)
        set(value) {
            if (value != field) {
                sp.edit().putBoolean("state", value).apply()
                field = value
            }
        }
}