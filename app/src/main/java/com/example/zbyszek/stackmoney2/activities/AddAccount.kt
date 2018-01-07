package com.example.zbyszek.stackmoney2.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.account.AccountSQL
import com.example.zbyszek.stackmoney2.model.account.BindedAccountSQL
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.activity_add_account.*
import org.jetbrains.anko.doAsync

class AddAccount : AppCompatActivity() {

    lateinit var database : AppDatabase
    lateinit var colors : Map<Int, String>

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)
        supportActionBar!!.setTitle(R.string.title_addAccount)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        databaseConnection()

        doAsync {
            colors = database.colorDAO().getAllColors().associateBy ( {it.id}, {it.value} )
        }

        button_confirm_new_account.setOnClickListener {
            addButtonOnClick()
        }
    }

    private fun addButtonOnClick(){
        val intent = Intent()

        val name = account_name_input.text.toString().trim()
        val colorId = account_colorId_input.text.toString().toInt()
        val parentAccountIdString = account_parentAccount_input.text.toString()
        val parentAccountId = if (parentAccountIdString.isEmpty()) null else parentAccountIdString.toLong()
        val userId = Preferences.getUserId(this)

        val bindedAccountSQL = BindedAccountSQL(userId, colorId, parentAccountId, name, colors[colorId]!!)
        doAsync {
            try {
                val id = database.accountDAO().insertAccountSQL(bindedAccountSQL.convertToAccountSQL())
                bindedAccountSQL.id = id
            } catch (e: Exception){
                runOnUiThread {
                    Toast.makeText(
                            applicationContext,
                            "Wystąpił błąd przy tworzeniu konta",
                            Toast.LENGTH_LONG).show()
                }
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            } finally {
                val bundle = Bundle()
                bundle.putSerializable("new_account", bindedAccountSQL.convertToAccount())
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(this)
    }
}
