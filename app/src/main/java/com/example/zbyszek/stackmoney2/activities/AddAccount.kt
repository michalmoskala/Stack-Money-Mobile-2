package com.example.zbyszek.stackmoney2.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.account.AccountSQL
import kotlinx.android.synthetic.main.activity_add_account.*

class AddAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)


    button_confirm_new_account.setOnClickListener {
        val intent = Intent()

        val colorId=account_colorId_input.text.toString().toInt()
        val name=account_name_input.text.toString()
        val parentAccount=account_parentAccount_input.text.toString().toLong()
        val userId = Preferences.getUserId(this)


        val account = AccountSQL(userId,colorId,parentAccount,name)
        val bundle = Bundle()
        bundle.putSerializable("new_account", account)


        intent.putExtras(bundle)
        setResult(0, intent)
        finish()

        }

    }
}
