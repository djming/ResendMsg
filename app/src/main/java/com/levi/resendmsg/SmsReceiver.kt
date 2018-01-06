package com.levi.resendmsg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.*
import android.util.Log
import java.lang.reflect.AccessibleObject.setAccessible
import android.widget.Toast
import android.text.TextUtils
import android.telephony.SmsManager.getSmsManagerForSubscriptionId

class SmsReceiver : BroadcastReceiver() {
    val TAG = "SmsReceiver"
    private val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    private lateinit var sp : SpHelper

    override fun onReceive(context: Context, intent: Intent) {
        sp = SpHelper(context)
        if (!sp.translate)
            return

        val action = intent.action;
        if (!action.equals(SMS_RECEIVED_ACTION))
            return

        val bundle = intent.extras
        val pdusData = bundle?.get("pdus") as Array<Any>
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
        //SendMsgHelper.sendMsgWithOneSIM(sp.target, from.toString(), content.toString())
        SendMsgHelper.sendMsgWithTwoSIM(context, sp.target, from.toString(), content.toString())
    }
}
