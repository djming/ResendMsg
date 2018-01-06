package com.levi.resendmsg.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.levi.resendmsg.R

/**
 * Created by levi on 2018/1/5.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */

object SendMsgHelper {
    //格式化转发短信
    private fun createMsg(from: String, content: String)
            = String.format("From:%s\r\nContent:%s", from, content)

    /**
     * @param context context
     * @param target 目标手机号码
     * @param from  发送方
     * @param content 短信内容
     *
     * 打开手机发送短信界面，需要手动点击"发送"按钮
     */
    fun sendMsgWithHand(context: Context, target: String, from: String, content: String) {
        val smsIntent = Intent(Intent.ACTION_VIEW)

        val msg = createMsg(from, content)
        smsIntent.setData(Uri.parse("smsto:"))
        smsIntent.setType("vnd.android-dir/mms-sms")
        smsIntent.putExtra("address", target)
        smsIntent.putExtra("sms_body", msg)
        context.startActivity(smsIntent)
    }

    /**
     * @param target 目标手机号码
     * @param from  发送方
     * @param content 短信内容
     *
     * 单卡手机发送短信
     */
    fun sendMsgWithOneSIM(target: String, from: String, content: String) {
        val smsManager = android.telephony.SmsManager.getDefault()
        //拆分短信内容（手机短信长度限制）
        val divideContents = smsManager.divideMessage(createMsg(from, content))

        for (text in divideContents) {
            smsManager.sendTextMessage(target, null, text, null, null)
        }
    }

    /**
     * @throws NullPointerException 不存在slotID对应的SIM卡(即不是双卡插入)
     * @param context context
     * @param target 目标手机号码
     * @param from  发送方
     * @param content 短信内容
     *
     * 单卡手机发送短信
     */
    @Throws(NullPointerException::class)
    fun sendMsgWithTwoSIM(context: Context, target: String, from: String, content: String) {
       if (getSIMInfo(context, 0) == TelephonyManager.SIM_STATE_READY) {
           sendMsgWithCertainSIM(context, 0, target, from, content)
       } else if (getSIMInfo(context, 1) == TelephonyManager.SIM_STATE_READY) {
           sendMsgWithCertainSIM(context, 1, target, from, content)
       }
    }

    @Throws(NullPointerException::class)
    fun sendMsgWithCertainSIM(context: Context, slotID: Int,
                                  target: String, from: String, content: String) {
        val sManager = context
                .getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        val list = sManager.activeSubscriptionInfoList
        val sInfo: SubscriptionInfo = list[slotID]
        val subId = sInfo.subscriptionId
        val manager = SmsManager
                .getSmsManagerForSubscriptionId(subId)
        manager.sendTextMessage(target, null,
                createMsg(from, content), null, null)
        Toast.makeText(context, context.resources.getString(R.string.sendingMsg),
                Toast.LENGTH_SHORT).show()
    }

    /**
     * @param context context
     * @param slotID 0 for SIM1 and 1 for SIM2
     *
     * @return TelephonyManager的SIM_STATE TelephonyManager.SIM_STATE_READY表示可用
     */
    @Throws(NullPointerException::class)
     fun getSIMInfo(context: Context, slotID: Int): Int {
        val mTelephonyManager = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val clz = mTelephonyManager.javaClass

        val mtd = clz.getMethod("getSimState", Int::class.javaPrimitiveType)
        mtd.isAccessible = true
        return mtd.invoke(mTelephonyManager, slotID) as Int
    }
}
