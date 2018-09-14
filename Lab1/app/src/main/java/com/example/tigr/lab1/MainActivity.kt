package com.example.tigr.lab1

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.view.View
import android.widget.Button
import android.widget.ProgressBar

class MainActivity : Activity() {
    private var notificationManager: NotificationManagerCompat?= null
    private var notification: Notification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = NotificationCompat.Builder(this, "").apply{
            setContentTitle("Test notification")
            setContentText("This is a test notification!")
            setSmallIcon(android.R.drawable.ic_notification_clear_all)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        notification = builder.build()
        notificationManager = NotificationManagerCompat.from(this)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onNotificationClick(v : View) {
        val button = v as Button
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        button.text = "10"
        object :CountDownTimer(10000, 1000) {
            override fun onTick(left: Long) {
                button.text = "${left/1000}"
                ++progressBar.progress
            }

            override fun onFinish() {
                button.text = getString(R.string.notif)
                progressBar.progress = 0
            }
        }.start()

        with(Handler(Looper.getMainLooper())) {
            post({ button.isEnabled = false })
            postDelayed({ notificationManager?.run { notify(0, notification!!) } }, 10000)
            postDelayed({ button.isEnabled = true }, 10000)
        }
    }
}
