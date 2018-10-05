package com.github.tigr.lab2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
	private var chosenNotification: Notification? = null
	private var chosenNotificationView: View? = null
    private var menu: Menu? = null

	private var cal: Calendar = Calendar.getInstance()
	private lateinit var dao: NotificationDao

	override fun onClick(view: View) {
		view.isActivated = true
		chosenNotificationView?.isActivated = false
		chosenNotificationView = view
		val isActivated = chosenNotificationView?.isActivated!!
		if (isActivated) {
			chosenNotification = (recyclerView.adapter as Adapter)
                .dataset[recyclerView.getChildViewHolder(view).layoutPosition]
		} else {
			chosenNotification = null
			chosenNotificationView = null
		}
		menu?.findItem(R.id.action_edit)?.isVisible = isActivated
		menu?.findItem(R.id.action_delete)?.isVisible = isActivated
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		fab.setOnClickListener {
			val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
		}
        calendarView.setOnDateChangeListener { cv, year, month, dayOfMonth ->
            cal.apply {
				set(Calendar.YEAR, year)
				set(Calendar.MONTH, month)
				set(Calendar.DAY_OF_MONTH, dayOfMonth)
			}
			//val notifications = dao.getOnDate(cal.timeInMillis)
			//(recyclerView.,dapter as Adapter).dataset = notifications
			showNotifications()
		}

		recyclerView.let {
			it.setHasFixedSize(true)
			it.layoutManager = LinearLayoutManager(this)
            it.adapter = Adapter(listOf(), this)
		}
		dao = NotificationDatabase.getInstance(applicationContext).notificationDao()
		Log.d("MainActivity", "Dao: $dao")
	}

	override fun onResume() {
		super.onResume()

		showNotifications()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_main, menu)

		menu.findItem(R.id.action_edit).setOnMenuItemClickListener {
			val intent = Intent(this, EditActivity::class.java).apply {
				putExtra("notification_id", chosenNotification!!.id)
				putExtra("notification_title", chosenNotification!!.title)
				putExtra("notification_description", chosenNotification!!.description)
				putExtra("notification_dateTime", chosenNotification!!.dateTime)
			}
			startActivity(intent)
            true
		}
		menu.findItem(R.id.action_delete).setOnMenuItemClickListener {
            dao.delete(chosenNotification!!)
			showNotifications()
			true
		}

		this.menu = menu
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId) {
			//R.id.action_settings -> true
			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun showNotifications() {
		cal.apply {
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
		}
		val notifications = dao.getOnDate(cal)
		(recyclerView.adapter as Adapter).dataset = notifications
		Log.d("showNotifications", "$cal")
		Log.d("showNotifications", "$dao")
		Log.d("showNotifications", "$notifications")
	}

}
