package com.example.zbyszek.stackmoney2.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(context, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        val dateString = "%04d-%02d-%02d".format(year,month+1,day)

        Toast.makeText(context, dateString, Toast.LENGTH_SHORT).show()
        val b: EditText = activity!!.findViewById(R.id.operation_date_input)

        b.setText(dateString)

    }

}