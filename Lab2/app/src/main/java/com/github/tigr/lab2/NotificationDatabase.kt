package com.github.tigr.lab2

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

@Database(entities=[Notification::class], version = 1)
@TypeConverters(Converters::class)
abstract class NotificationDatabase : RoomDatabase() {
    companion object {
        private var instance: NotificationDatabase? = null

        fun getInstance(ctx: Context? = null): NotificationDatabase {
            if (ctx != null && instance == null)
                synchronized(NotificationDatabase::class) {
                    if (instance == null)
                    instance = Room.databaseBuilder(
                        ctx,
                        NotificationDatabase::class.java,
                        "notification-db"
                    ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }

    abstract fun notificationDao(): NotificationDao
}