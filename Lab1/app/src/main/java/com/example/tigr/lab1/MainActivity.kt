package com.example.tigr.lab1

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

class MainActivity : Activity() {
    private var notificationManager: NotificationManagerCompat?= null
    private var notification: Notification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

		var nChannelId = ""
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val notificationChannel = NotificationChannel("com.example.tigr.lab1", 
				"Lab1", NotificationManager.IMPORTANCE_DEFAULT)
			notificationChannel.setDescription("Notification channel for Lab1 application")
			val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(notificationChannel)
			nChannelId = notificationChannel.id
		} 
		notificationManager = NotificationManagerCompat.from(this)
        notification = NotificationCompat.Builder(this, nChannelId).apply{
            setContentTitle("Test notification")
            setContentText("This is a test notification!")
            setSmallIcon(android.R.drawable.ic_notification_clear_all)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }.build()
        //notification = builder.build()
		//notificationManager.createNotificationChannel()

		findViewById<Button>(R.id.btn_search).setOnClickListener({
			val query = findViewById<EditText>(R.id.et_search).text.toString()
			val address = "http://google.com/search?q=$query"
			val intent = Intent("android.intent.action.VIEW", Uri.parse(address))
			if (intent.resolveActivity(getPackageManager()) != null) 
				startActivity(intent)
		})
    }

    @Suppress("UNUSED_PARAMETER")
    fun onNotificationClick(v : View) {
        val button = v as Button
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        button.text = "10"
        object :CountDownTimer(10000, 16) {
            override fun onTick(left: Long) {
                button.text = "${left/1000}"
                progressBar.progress += 17
            }

            override fun onFinish() {
                button.text = getString(R.string.notif)
                progressBar.progress = 0
            }
        }.start()

        with(Handler()) {
            post({ button.isEnabled = false })
            postDelayed({ notificationManager?.run { notify(0, notification!!) } }, 10000)
            postDelayed({ button.isEnabled = true }, 10000)
        }
    }
}
