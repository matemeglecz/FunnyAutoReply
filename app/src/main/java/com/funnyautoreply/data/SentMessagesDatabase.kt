package com.funnyautoreply.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 2)
abstract class SentMessagesDatabase : RoomDatabase(){
    abstract fun messageDao(): MessageDao

    companion object {
        fun getDatabase(applicationContext: Context): SentMessagesDatabase {
            return Room.databaseBuilder(
                applicationContext,
                SentMessagesDatabase::class.java,
                "sent-messages"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }

}