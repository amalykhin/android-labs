package com.github.tigr.lab2

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import java.time.LocalDateTime
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var cal: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        cal = arguments?.get("calendar") as Calendar
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        /*if (activity is AddActivity) {
            (activity as AddActivity).dateTime.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        } else if (activity is EditActivity)
            (activity as EditActivity).dateTime.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            */
        with(cal) {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        activity?.findViewById<Button>(R.id.button_date)?.text = "Date $dayOfMonth.$month.$year"
    }
}