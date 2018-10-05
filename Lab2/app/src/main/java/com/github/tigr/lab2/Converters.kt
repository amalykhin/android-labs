package com.github.tigr.lab2

import android.arch.persistence.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun dateTimeToTimestamp(dt: Calendar): Long? {
        return dt.timeInMillis/1000
    }

    @TypeConverter
    fun fromTimestamp(ts: Long): Calendar? {
        val cal = Calendar.getInstance().apply {
            timeInMillis = ts*1000
        }
        return cal
    }
}