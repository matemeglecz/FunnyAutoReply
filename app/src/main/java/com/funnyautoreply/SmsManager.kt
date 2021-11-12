package com.funnyautoreply

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import androidx.preference.PreferenceManager

import android.provider.ContactsContract.PhoneLookup

import android.net.Uri
import android.database.Cursor

import android.provider.MediaStore

import android.content.ContentResolver
import android.provider.ContactsContract


object SmsManager {
    private val smsManager : SmsManager = SmsManager.getDefault() //android 31-re lett deprecated

    fun sendSms(context: Context, incomingNumber: String?, jokeWrapper: JokeWrapper?) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val replyCategory = sharedPref.getString("reply_category", null) ?: return

        if(replyCategory == "reply_contacts" && !numberInContacts(context, incomingNumber)
            || replyCategory == "reply_starred_contacts" && !numberInStarredContacts(context, incomingNumber))
            return


        Log.d("SMS_INFO", "SMS to $incomingNumber joke: ${jokeWrapper?.getJoke()}")

        val joke=jokeWrapper?.getJoke()

        if(incomingNumber != null && joke != null)
            smsManager.sendTextMessage(incomingNumber, null, joke, null, null)
    }


    private fun numberInContacts(context: Context, number: String?) : Boolean{
        val lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        val mPhoneNumberProjection = arrayOf(PhoneLookup._ID, PhoneLookup.NUMBER)
        val cursorNumbers = context.contentResolver.query(lookupUri, mPhoneNumberProjection, null, null, null)
        cursorNumbers.use { cur ->
            if (cur!!.moveToFirst()) {
                return true
            }
        }
        return false
    }


    private fun numberInStarredContacts(context: Context, number: String?) : Boolean{
        val lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        val phoneNumberProjection = arrayOf(PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.STARRED)
        val starredSelection = PhoneLookup.STARRED + "=?"
        val starredArg = arrayOf("1")
        val cursorNumbers = context.contentResolver.query(lookupUri, phoneNumberProjection, starredSelection, starredArg, null)
        cursorNumbers.use { cursor ->
            if (cursor!!.moveToFirst()) {
                val colIndex=cursor.getColumnIndex(ContactsContract.PhoneLookup.STARRED)
                if (colIndex >= 0 && cursor.getInt(colIndex) == 1)
                    return true
                /*while (!cursor.isAfterLast()) {
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.PhoneLookup.STARRED)) == 1) {
                        Log.d("STARRED","OUTPUT: " + cursor.getInt(0) );
                        return true;
                    }
                    cursor.moveToNext();
                }*/
            }

        }

        return false
    }

}