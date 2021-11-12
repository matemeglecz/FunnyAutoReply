package com.funnyautoreply.network

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.funnyautoreply.model.JokeData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit: Retrofit
    private val jokeApi: JokeApi

    private const val SERVICE_URL = "https://v2.jokeapi.dev/"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        jokeApi = retrofit.create(JokeApi::class.java)
    }

    fun getJoke(context: Context): Call<JokeData?>? {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val customCategory = sharedPref.getBoolean("any_category", false)

        //ha nincs kategória
        if(!customCategory)
            return jokeApi.getJokeAny()
        //ha vannak kategóriák
        //összefűzni stringre és getJokeCustom hívás
        else{
            val categories: Set<String>? = sharedPref.getStringSet("categories", null)
            val categoriesString : String
            if (categories != null) {
                for(s : String in categories){
                    categoriesString = categories.joinToString(",")
                    Log.d("CAT_STR", categoriesString)
                    return jokeApi.getJokeCustom(categoriesString)
                }
            }
        }
        return null
    }
}