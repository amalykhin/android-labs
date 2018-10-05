package com.github.tigr.lab2

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Notification (
    var title: String,
    var description: String,
    var dateTime: Calendar,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)