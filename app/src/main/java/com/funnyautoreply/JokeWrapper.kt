package com.funnyautoreply

import com.funnyautoreply.model.JokeData

class JokeWrapper (private val jokeData: JokeData?) {

    fun getJoke() : String? {
        if(jokeData==null || jokeData.error)
            return null

        if(jokeData.type=="single")
            return jokeData.joke;
        else if(jokeData.type=="twopart")
            return jokeData.setup + "\n" + jokeData.delivery;

        return null
    }

    fun getCategory() : String? {
        return jokeData?.category
    }
}