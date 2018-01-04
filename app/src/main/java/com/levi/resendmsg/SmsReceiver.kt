package com.levi.resendmsg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsMessage
import android.util.Log
import android.R.attr.phoneNumber
import android.R.id.message
import android.content.IntentFilter
import android.widget.Toast
import android.app.Activity
import android.app.PendingIntent
import android.telephony.SmsManager


class SmsReceiver : BroadcastReceiver() {
    val TAG = "SmsReceiver"
    private val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    private lateinit var sp : SpHelper

    override fun onReceive(context: Context, intent: Intent) {
        sp = SpHelper(context)
        Log.d(TAG, "REceive Msg")
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
        Log.d(TAG, "from" + from.toString() + "\r\nContent:"+content.toString())
        sendMsg2(context, from.toString(), content.toString())
    }

    private fun sendMsg(context: Context, from : String, content : String) {
        Log.d(TAG, "Send Msg")
        val smsIntent = Intent(Intent.ACTION_VIEW)
        val msg = String.format("From:%s\r\nContent:%s", from, content)

        smsIntent.setData(Uri.parse("smsto:"))
        smsIntent.setType("vnd.android-dir/mms-sms")
        smsIntent.putExtra("address", sp.target)
        smsIntent.putExtra("sms_body", msg)
        context.startActivity(smsIntent)
    }

    private fun sendMsg2 (context: Context, from : String, content : String) {
        val smsManager = android.telephony.SmsManager.getDefault()
        //拆分短信内容（手机短信长度限制）
        val divideContents = smsManager.divideMessage(content)

        for (text in divideContents) {
            smsManager.sendTextMessage(from, null, text, null, null)
        }
    }
}
