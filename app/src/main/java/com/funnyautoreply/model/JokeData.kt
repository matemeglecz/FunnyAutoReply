package com.funnyautoreply.model

import android.media.MediaRouter

data class JokeData(
    var error: Boolean,
    var category: String,
    var type: String,
    var joke: String?,
    var safe: Boolean,
    var setup: String?,
    var delivery: String?


)
