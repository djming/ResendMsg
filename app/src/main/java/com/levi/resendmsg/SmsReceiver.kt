package com.levi.resendmsg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    private val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"

    override fun onReceive(context: Context, intent: Intent) {
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
            content.append(temp?.messageBody);
            from.append(temp?.originatingAddress);
        }

    }
}
