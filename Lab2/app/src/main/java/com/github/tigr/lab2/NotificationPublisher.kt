package com.github.tigr.lab2

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log

class NotificationPublisher : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        val dao = NotificationDatabase.getInstance(ctx!!.applicationContext).notificationDao()
        val notificationDomain: Notification? = dao.get(intent!!.getIntExtra("notification_id", -1))
        notificationDomain ?: return
        Log.d("NotificationDao", "$dao")
        Log.d("Notification", "$notificationDomain")
        val notification = NotificationCompat.Builder(ctx!!, ctx!!.packageName).run {
            setContentTitle(notificationDomain.title)
            setContentText(notificationDomain.description)
            setSmallIcon(android.R.drawable.sym_def_app_icon)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            build()
        }
        (ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .notify(notificationDomain.id, notification)
    }
}