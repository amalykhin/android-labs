package com.github.tigr.lab2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity : AppCompatActivity() {
	private var dao = NotificationDatabase.getInstance().notificationDao()
	private var cal = Calendar.getInstance()

	//var dateTime = Calendar.getInstance()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add)

        val args = Bundle().apply {
			putSerializable("calendar", cal)
		}

        button_date.setOnClickListener {
			DatePickerFragment().apply {
				arguments = args
				show(supportFragmentManager, "datePicker")
			}
		}
		button_time.setOnClickListener {
			TimePickerFragment().apply {
				arguments = args
				show(supportFragmentManager, "timePicker")
			}
		}
		button_edit_notification.setOnClickListener {
			val title = edit_notification_name.text.toString()
			val des   = edit_notification_description.text.toString()

			var notification = Notification(title, des, cal)
			notification = dao.run {
				insert(notification)
				getMostRecent()
			}

            val intent = Intent(this, NotificationPublisher::class.java).apply {
                putExtra("notification_id", notification.id)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    notification.id,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT
            )
            (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                    .set(AlarmManager.RTC_WAKEUP, notification.dateTime.timeInMillis, pendingIntent)

            //Toast.makeText(this, "id = ${notification.id}", Toast.LENGTH_SHORT).show()

            this.finish()
		}
	}
}
