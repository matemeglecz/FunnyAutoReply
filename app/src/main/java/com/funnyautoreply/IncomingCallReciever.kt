package com.funnyautoreply

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import android.util.Log
import android.telephony.PhoneStateListener
import androidx.preference.PreferenceManager
import com.funnyautoreply.model.JokeData
import com.funnyautoreply.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncomingCallReceiver : BroadcastReceiver() {
    companion object {
        private var previousState: Int? = TelephonyManager.CALL_STATE_IDLE
        private var incomingNumber: String? = null
    }

    private lateinit var sharedPref : SharedPreferences

    override fun onReceive(context: Context, intent: Intent) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        Log.d("BR", "BR started")

        if (intent.action.equals("android.intent.action.PHONE_STATE") && sharedPref.getBoolean("reply_on_off", false)) {
            phoneStateListener(context)
        }
    }

    private fun phoneStateListener(context: Context){ //android 31-re lett deprecated
        val telephony = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                super.onCallStateChanged(state, phoneNumber)
                if(state == TelephonyManager.CALL_STATE_RINGING)
                    incomingNumber = phoneNumber

                if(previousState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE)
                    sendJoke(context)

                previousState=state
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun sendJoke(context: Context) {
        NetworkManager.getJoke(context)?.enqueue(object : Callback<JokeData?> {
            override fun onResponse(
                call: Call<JokeData?>,
                response: Response<JokeData?>
            ) {
                Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful) {
                    //jokeWrapper=JokeWrapper(response.body());
                    SmsManager.sendSms(context, incomingNumber, JokeWrapper(response.body()))
                } else {
                    Log.d("ERROR_IN_MSG", "Error: " + response.message())
                }
            }

            override fun onFailure(
                call: Call<JokeData?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Log.d("ERROR_IN_MSG", "Network request error occured, check LOG")
            }
        })
    }

}