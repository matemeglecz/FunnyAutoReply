package com.funnyautoreply.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "joke") var joke: String,
    @ColumnInfo(name = "joke") var phoneNumber: String)
