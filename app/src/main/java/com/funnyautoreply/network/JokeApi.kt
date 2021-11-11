package com.funnyautoreply.network

import com.funnyautoreply.model.JokeData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JokeApi {
    @GET("/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit")
    fun getJokeAny(): Call<JokeData?>?

    @GET("/joke/{categories}?blacklistFlags=nsfw,religious,political,racist,sexist,explicit")
    fun getJokeCustom(@Path(encoded=false, value="categories") categories: String): Call<JokeData?>?
}