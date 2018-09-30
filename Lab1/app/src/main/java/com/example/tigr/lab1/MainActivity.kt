package com.example.tigr.lab1

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import java.io.File
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private object PermissionRequest {
        const val CAMERA = 0
    }

    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var notification: Notification
    private var photoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

		val nChannelId = resources.getString(R.string.notif_channel_id)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val notificationChannel = NotificationChannel(nChannelId, 
				resources.getString(R.string.notif_channel_name),
				NotificationManager.IMPORTANCE_HIGH
			).apply {
				description = """Notification channel
                                | for Lab1 application""".trimMargin()
			}
			val notificationManager = 
				getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(notificationChannel)
		} 

		notificationManager = NotificationManagerCompat.from(this)
        notification = NotificationCompat.Builder(this, nChannelId).apply {
            setContentTitle("Test notification")
            setContentText("This is a test notification!")
            setSmallIcon(android.R.drawable.ic_notification_clear_all)
			setVibrate(longArrayOf(0, 500, 0, 0))
			setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            priority = NotificationCompat.PRIORITY_MAX
        }.build()

		button_search.setOnClickListener {
			val query = edit_search.text.toString()
			val intent = Intent(Intent.ACTION_VIEW,
				Uri.parse("http://google.com/search?q=$query")
			)
			if (intent.resolveActivity(packageManager) != null)
				startActivity(intent)
		}
        button_surprise.setOnClickListener {
            video_rave.setVideoURI(Uri.parse("android.resource://$packageName/raw/rave"))
            if (video_rave.visibility != View.VISIBLE) {
                with (video_rave) {
                    button_surprise.text = "Stop"
                    seekTo(76000)
                    visibility = View.VISIBLE
                    start()
                }
            } else {
                with (video_rave) {
                    button_surprise.text = "Rave"
                    stopPlayback()
                    visibility = View.GONE
                }
            }
        }
		button_camera_shoot.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, 
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        PermissionRequest.CAMERA
                )
                return@setOnClickListener
            }

            val checkedId = radio_camera_type.checkedRadioButtonId
            val intent    = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoUri = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp").let {
                it.createNewFile()
                photoPath = it.absolutePath
                FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                )
            }
            if (intent.resolveActivity(packageManager) == null)
                return@setOnClickListener
            intent.apply {
                putExtra(
                    "android.intent.extras.CAMERA_FACING",
                    if (checkedId == R.id.radio_camera_facing) 1 else 0
                )
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
		}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode  != RESULT_OK) {
            Toast.makeText(
                    this,
                    "Camera activity failed",
                    Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intent = Intent(this, PhotoActivity::class.java).apply {
            putExtra("path", photoPath)
        }

        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, 
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionRequest.CAMERA ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    button_camera_shoot.performClick()
        }
    }

    fun onNotificationClick(v: View) {
        val button 		= v as Button

        object : CountDownTimer(10000, 16) {
            override fun onTick(left: Long) {
                button.text 		 = "${ceil(left / 1000f).toInt()}"
                progress_notification.progress = 10000 - left.toInt()
            }

            override fun onFinish() {
                button.text 		 = getString(R.string.action_notify)
                progress_notification.progress = 0
            }
        }.start()

		button.isEnabled = false
		if (this::notificationManager.isInitialized &&
			this::notification.isInitialized
		) { 
			Handler().postDelayed({ 
				notificationManager.notify(0, notification)
				button.isEnabled = true
			}, 10000)
		}
    }
}
