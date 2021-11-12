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
import com.funnyautoreply.adapter.MessageAdapter
import com.funnyautoreply.data.Message
import com.funnyautoreply.data.SentMessagesDatabase
import com.funnyautoreply.model.JokeData
import com.funnyautoreply.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.thread

class IncomingCallReceiver : BroadcastReceiver() {
    companion object {
        private var previousState: Int? = TelephonyManager.CALL_STATE_IDLE
        private var incomingNumber: String? = null
    }


    private lateinit var sharedPref : SharedPreferences
    private lateinit var database: SentMessagesDatabase
    private lateinit var adapter: MessageAdapter

    override fun onReceive(context: Context, intent: Intent) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        Log.d("BR", "BR started")

        database = SentMessagesDatabase.getDatabase(context)
        adapter = MessageAdapter()

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
                    val jokeWrapper=JokeWrapper(response.body())
                    val number= incomingNumber
                    if(SmsManager.sendSms(context, incomingNumber, jokeWrapper)
                        && jokeWrapper.getJoke() != null
                        && number !=null){
                        val msg=Message(
                            phoneNumber = number,
                            joke = jokeWrapper.getJoke().toString(),
                            category = jokeWrapper.getCategory() ?: "",
                            date = Message.fromCalendar(Calendar.getInstance()) ?: 0
                        )
                        addToDatabase(msg)
                    }
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

    private fun addToDatabase(newItem: Message){
        thread {
            val insertId = database.messageDao().insert(newItem)
            newItem.id = insertId
            adapter.addItem(newItem)
            adapter.notifyDataSetChanged()
        }
    }

}