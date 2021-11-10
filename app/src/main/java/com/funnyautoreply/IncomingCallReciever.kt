package com.funnyautoreply

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.telephony.PhoneStateListener
import android.widget.Toast

class IncomingCallReceiver : BroadcastReceiver() {
    companion object {
        var previousState: Int? = TelephonyManager.CALL_STATE_IDLE//TelephonyManager.EXTRA_STATE_IDLE
        var incomingNumber: String? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BR", "BR started")

        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            phoneStateListener(context)
            /*val phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Toast.makeText(context, phoneState + " " + previousState, Toast.LENGTH_LONG).show()
            if(phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING))
                incomingNumber=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if(phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE) &&
                previousState.equals(TelephonyManager.EXTRA_STATE_RINGING)) { //nem vették fel a telefont vagy kinyomták
                Toast.makeText(context, "nem fogadott hívás", Toast.LENGTH_LONG).show()
                Log.d("valami", "nem fogadott hívás")
                onMissedCall(context)
            }
            previousState=phoneState*/

        }
    }

    private fun onMissedCall(context: Context) {
        /*val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault();
        }*/
        val smsManager=SmsManager.getDefault(); //android 31-re lett deprecated
        Toast.makeText(context, "SMS to $incomingNumber", Toast.LENGTH_LONG).show()
        if(incomingNumber!=null)
            smsManager?.sendTextMessage(incomingNumber, null, "test test test test ", null, null)
    }

    private fun phoneStateListener(context: Context){ //android 31-re lett deprecated
        val telephony = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                super.onCallStateChanged(state, phoneNumber)
                if(state == TelephonyManager.CALL_STATE_RINGING) {
                    incomingNumber = phoneNumber
                    Log.d("INCOMINGNUM",phoneNumber)
                }

                if(previousState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE)
                    onMissedCall(context)

                previousState=state
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }
}