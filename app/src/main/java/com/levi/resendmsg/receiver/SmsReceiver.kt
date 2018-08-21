package com.levi.resendmsg.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.levi.resendmsg.helper.SendMsgHelper
import com.levi.resendmsg.helper.SpHelper

/**
 * Created by levi on 2018/1/4.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */

class SmsReceiver : BroadcastReceiver() {
    val TAG = "SmsReceiver"
    private val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    private lateinit var sp : SpHelper

    override fun onReceive(context: Context, intent: Intent) {
        sp = SpHelper(context)
        if (!sp.state)
            return

        val action = intent.action;
        if (!action.equals(SMS_RECEIVED_ACTION))
            return

        val bundle = intent.extras
        val pdusData = bundle?.get("pdus") as Array<*>
        val msg = arrayOfNulls<SmsMessage>(pdusData.size)
        for ( i in 0 until msg.size){
            val pdus = pdusData[i] as ByteArray
            msg[i] = SmsMessage.createFromPdu(pdus)
        }
        val content = StringBuilder()
        val from = StringBuilder()
        for (temp in msg){
            content.append(temp?.messageBody)
            from.append(temp?.originatingAddress)
        }
        if (sp.slotID == -1) {
            SendMsgHelper.sendMsgWithTwoSIM(context, sp.target, from.toString(), content.toString())
        } else {
            SendMsgHelper.sendMsgWithCertainSIM(context, 1, sp.target, from.toString(), content.toString())
        }
    }
}
