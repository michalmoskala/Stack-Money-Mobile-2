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
import com.example.zbyszek.stackmoney2.fragments.DatePickerFragment
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.operation.Operation
import kotlinx.android.synthetic.main.activity_add_operation.view.*




class AddOperation() : AppCompatActivity() {

    fun showTimePickerDialog(v: View) {

        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "timePicker")


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_operation)




        operation_button_confirm_new_operation.setOnClickListener {
            val intent = Intent()

            val userId = Preferences.getUserId(this)
            val title = operation_name_input.text.toString()
            //val cost = (amount_input.text.toString().toDouble() * 100).toInt()
            val cost=operation_amount_input.text.toString().toInt()
            val isExpense = operation_isExpense_input.isChecked
            val description = operation_description_input.text.toString()
            val account = operation_account_input.text.toString().toLong()
            val category = operation_category_input.text.toString().toLong()
            val visibleInStatistics=operation_visibleInStatistics_input.isChecked
            val date=operation_date_input.text.toString()

            val operation = Operation(userId, account, category, title, cost, isExpense, visibleInStatistics, description,date )
            val bundle = Bundle()
            bundle.putSerializable("new_operation", operation)

            intent.putExtras(bundle)
            setResult(0, intent)
            finish()


        }
    }
}
