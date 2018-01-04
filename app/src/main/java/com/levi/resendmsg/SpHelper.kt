package com.levi.resendmsg

import android.content.Context

/**
 * Created by levi on 2018/1/4.
 * Copyright © 2017 levi.
 * All rights reserved.
 */
 
class SpHelper(ctx : Context) {
    private val defaultTarget = "15527163373"

    private val sp by lazy {
        ctx.getSharedPreferences("", Context.MODE_PRIVATE)
    }

    var target = defaultTarget
        get() = sp.getString("target", defaultTarget)
        set(value) {
            if (!value.equals(field)) {
                sp.edit().putString("target", value).apply()
                field = value
            }
        }

    var translate = false
        get() = sp.getBoolean("", false)
        set(value : Boolean) {
            if (value != field) {
                sp.edit().putBoolean("translate", value).apply()
                field = value
            }
        }
}