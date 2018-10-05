package com.github.tigr.lab2

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.notification_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class Adapter(data: List<Notification>, private val activity: MainActivity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var dataset = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class ViewHolder(val view: ViewGroup) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.notification_view, parent, false) as ViewGroup
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notif = dataset[position]
        val time  = SimpleDateFormat("HH:mm").format(notif.dateTime.time)
        val date  = notif.id//SimpleDateFormat("yyyy.MM.dd").format(notif.dateTime.time)
        holder.view.setOnClickListener(activity)
        holder.view.text_date.text   = date.toString()
        holder.view.text_time.text   = time
        holder.view.text_title.text  = notif.title
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}