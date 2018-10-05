package com.github.tigr.lab2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {
	private var dao = NotificationDatabase.getInstance().notificationDao()
	private lateinit var cal: Calendar

	//var dateTime = Calendar.getInstance()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add)

		val id = intent.getIntExtra("notification_id", -1)
		val notification = dao.get(id)
		edit_notification_name.setText(notification.title)
		edit_notification_description.setText(notification.description)
		//edit_notification_name.setText(intent.getStringExtra("notification_title"))
		//edit_notification_description.setText(intent.getStringExtra("notification_description"))
		//cal = intent.getSerializableExtra("notification_dateTime") as Calendar

		val args = Bundle().apply {
			putSerializable("calendar", notification.dateTime)
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
			with(notification) {
				title = edit_notification_name.text.toString()
				description = edit_notification_description.text.toString()
			}

			dao.update(notification)
			this.finish()
		}
	}
}
