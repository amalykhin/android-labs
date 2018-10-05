package com.github.tigr.lab2

import android.arch.persistence.room.*
import java.util.*

@Dao
interface NotificationDao {
    @Insert
    fun insert(n: Notification)

    @Delete
    fun delete(vararg n: Notification)

    @Query("SELECT * FROM Notification WHERE id = :id")
    fun get(id: Int): Notification

    @Query("SELECT * FROM Notification")
    fun getAll(): List<Notification>

    @Query("SELECT * FROM Notification WHERE julianday(dateTime,'unixepoch')-julianday(:date,'unixepoch') BETWEEN 0 AND 1")
    fun getOnDate(date: Calendar): List<Notification>

    @Query("SELECT * FROM Notification ORDER BY dateTime DESC LIMIT 1")
    fun getMostRecent(): Notification

    @Update
    fun update(new: Notification)
}