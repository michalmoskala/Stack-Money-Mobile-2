package com.example.zbyszek.stackmoney2.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.zbyszek.stackmoney2.R
import android.content.Intent
import com.example.zbyszek.stackmoney2.model.operation.Operation
import kotlinx.android.synthetic.main.activity_add_operation.*
import java.lang.Integer.parseInt


class AddOperation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_operation)


        button_confirm_new_operation.setOnClickListener {
            val intent = Intent()

            val title=name_input.text.toString()
            val cost=parseInt(ammount_input.text.toString())
            val isExpence=isExpence_input.isChecked
            val description=description_input.text.toString()


            val operation= Operation(1, 1, 1, 1, title, cost, isExpence, true, description, "2015-01-01")
            val bundle = Bundle()

            bundle.putSerializable("data",operation)

            intent.putExtras(bundle)
            setResult(0,intent)
            finish()

        }


    }
}
