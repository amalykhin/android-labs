package com.github.tigr.lab2

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.Button
import android.widget.TimePicker
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private lateinit var cal: Calendar
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        cal = arguments?.get("calendar") as Calendar
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity,
             this,
             hour,
             minute,
             DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
       /*if (activity is AddActivity) {
           (activity as AddActivity).dateTime.apply {
               set(Calendar.HOUR_OF_DAY, hourOfDay)
               set(Calendar.MINUTE, minute)
           }
       } else if (activity is EditActivity) {
           (activity as EditActivity).dateTime.apply {
               set(Calendar.HOUR_OF_DAY, hourOfDay)
               set(Calendar.MINUTE, minute)
           }
       }
       */
        with(cal) {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        activity?.findViewById<Button>(R.id.button_time)?.text = "Time $hourOfDay:$minute"
    }
}