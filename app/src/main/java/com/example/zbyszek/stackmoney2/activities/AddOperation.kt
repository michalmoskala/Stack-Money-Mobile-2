package com.example.zbyszek.stackmoney2.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.support.v4.app.DialogFragment
import android.os.Bundle
import com.example.zbyszek.stackmoney2.R
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_operation.*
import android.view.View
import android.widget.Button
import java.util.*
import android.widget.DatePicker
import android.widget.Toast
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.operation.Operation
import kotlinx.android.synthetic.main.activity_add_operation.view.*


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
        val b:Button=activity!!.findViewById(R.id.date_input)

        b.text = dateString

    }

}

class AddOperation() : AppCompatActivity() {

    fun showTimePickerDialog(v: View) {

        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "timePicker")


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_operation)




        button_confirm_new_operation.setOnClickListener {
            val intent = Intent()

            val userId = Preferences.getUserId(this)
            val title = name_input.text.toString()
            //val cost = (amount_input.text.toString().toDouble() * 100).toInt()
            val cost=amount_input.text.toString().toInt()
            val isExpense = isExpense_input.isChecked
            val description = description_input.text.toString()
            val account = account_input.text.toString().toLong()
            val category = category_input.text.toString().toLong()
            val visibleInStatistics=visibleInStatistics_input.isChecked
            val date=date_input.text.toString()

            val operation = Operation(userId, account, category, title, cost, isExpense, visibleInStatistics, description,date )
            val bundle = Bundle()
            bundle.putSerializable("new_operation", operation)

            intent.putExtras(bundle)
            setResult(0, intent)
            finish()


        }
    }
}
