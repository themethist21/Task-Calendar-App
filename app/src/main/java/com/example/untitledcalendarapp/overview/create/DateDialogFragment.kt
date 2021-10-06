    package com.example.untitledcalendarapp.overview.create

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker

import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.untitledcalendarapp.R
import java.util.*

class DateDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel:CreateViewModel by activityViewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(this.requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        viewModel.setDate(year,month,day)
    }
}
