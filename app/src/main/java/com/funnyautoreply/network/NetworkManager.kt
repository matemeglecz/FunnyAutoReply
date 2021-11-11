package com.funnyautoreply.network

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

    fun getJoke(): Call<JokeData?>? {
        //ha nincs kategória
        return jokeApi.getJokeAny()

        //ha vannak kategóriák
        //összefűzni stringre és getJokeCustom hívás
    }
}