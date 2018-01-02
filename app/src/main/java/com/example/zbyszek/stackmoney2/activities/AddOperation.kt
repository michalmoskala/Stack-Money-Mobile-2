package com.example.zbyszek.stackmoney2.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.zbyszek.stackmoney2.R
import android.content.Intent
import android.widget.Toast
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.operation.Operation
import kotlinx.android.synthetic.main.activity_add_operation.*


class AddOperation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_operation)

        button_confirm_new_operation.setOnClickListener {
            val intent = Intent()

            val userId = Preferences.getUserId(this)
            val title = name_input.text.toString()
            val cost = (amount_input.text.toString().toDouble() * 100).toInt()
            val isExpense = isExpence_input.isChecked
            val description = description_input.text.toString()

            val operation = Operation(userId, 1, 1, title, cost, isExpense, true, description, "2015-01-01")
            val bundle = Bundle()
            bundle.putSerializable("new_operation", operation)

            intent.putExtras(bundle)
//            setResult(0, intent)
            if (parent == null) {
                setResult(0, intent)
            } else {
                parent.setResult(0, intent)
            }
            finish()
        }
    }
}
