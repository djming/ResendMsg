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
        sendMsgWithOneSIM(context, from.toString(), content.toString())
    }

    private fun sendMsgWithHand(context: Context, from : String, content : String) {
        Log.d(TAG, "Send Msg")
        val smsIntent = Intent(Intent.ACTION_VIEW)
        val msg = String.format("From:%s\r\nContent:%s", from, content)

        smsIntent.setData(Uri.parse("smsto:"))
        smsIntent.setType("vnd.android-dir/mms-sms")
        smsIntent.putExtra("address", sp.target)
        smsIntent.putExtra("sms_body", msg)
        context.startActivity(smsIntent)
    }

    private fun sendMsgWithOneSIM(context: Context, from : String, content : String) {
        val smsManager = android.telephony.SmsManager.getDefault()
        //拆分短信内容（手机短信长度限制）
        val divideContents = smsManager.divideMessage(content)

        for (text in divideContents) {
            smsManager.sendTextMessage(from, null, text, null, null)
        }
    }

    private fun sendMsgWithTwoSIM (context: Context, from : String, content : String) {
        val mTelephonyManager = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val clz = mTelephonyManager.javaClass

        val mtd = clz.getMethod("getSimState", Int::class.javaPrimitiveType)
        mtd.isAccessible = true
        //slotID 0 for SIM1 and 1 for SIM2
        val slotID = 0
        val status = mtd.invoke(mTelephonyManager, slotID) as Int

        var sInfo: SubscriptionInfo? = null

        val sManager = context
                .getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        val list = sManager.activeSubscriptionInfoList
        if (list.size == 2) {// double card
            sInfo = list.get(0)
        } else {//single card
            sInfo = list.get(0);
        }
        if (sInfo != null) {
//            mcc 460 for china
//            46000,      中国移动TD    10086
//            46001,      中国联通      10010
//            46002,      中国移动GSM   10086
//            46003,      中国电信CDMA  10000
            val provider = sInfo.mcc.toString() + "0" + sInfo.mnc.toString()
            val subId = sInfo.subscriptionId
            Log.i(TAG, " select provider = " + provider + ", subid = "
                    + subId)

            val manager = SmsManager
                    .getSmsManagerForSubscriptionId(subId)

            if (!TextUtils.isEmpty(provider)) {
                manager.sendTextMessage(provider, null, "YE", null, null)
                Toast.makeText(context, "信息正在发送，请稍候", Toast.LENGTH_SHORT)
                        .show()
            } else {
                Toast.makeText(context, "无法正确的获取SIM卡信息，请稍候重试",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
}
