package com.funnyautoreply.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.*

@Entity(tableName = "message")
data class Message(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "joke") var joke: String,
    @ColumnInfo(name = "phoneNumber") var phoneNumber: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "date") var date: Long
    ){
    companion object {
        @TypeConverter
        fun toCalendar(l: Long?): Calendar? {
            val c = Calendar.getInstance()
            c.timeInMillis = l!!
            return c
        }

        @TypeConverter
        fun fromCalendar(c: Calendar?): Long? {
            return c?.time?.time
        }

        @TypeConverter
        fun toFormattedDate(l: Long?): String? {
            val sdf = DateFormat.getDateTimeInstance()
            var strdate : String? = null
            val date=toCalendar(l)

            if (date != null) {
                strdate = sdf.format(date.time)
            }
            return strdate
        }
    }
}
