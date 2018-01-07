package com.example.zbyszek.stackmoney2.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.fragments.DatePickerFragment
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import com.example.zbyszek.stackmoney2.model.operation.Operation
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.activity_add_operation.*
import org.jetbrains.anko.doAsync


class AddOperation() : AppCompatActivity() {
    lateinit var database : AppDatabase

    fun showTimePickerDialog(v: View) {

        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "timePicker")


    }

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
        setContentView(R.layout.activity_add_operation)
        supportActionBar!!.setTitle(R.string.title_addOperation)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        databaseConnection()

        operation_button_confirm_new_operation.setOnClickListener {
            addButtonOnClick()
        }
    }

    private fun addButtonOnClick() {
        val intent = Intent()

        val userId = Preferences.getUserId(this)
        val title = operation_name_input.text.toString()
        //val cost = (amount_input.text.toString().toDouble() * 100).toInt()
//            val cost = operation_amount_input.text.toString().toInt()
        val cost = (operation_amount_input.text.toString().toDouble() * 100).toInt()
        val isExpense = radio_isExpense.isChecked
        val description = operation_description_input.text.toString()
        val accountId = operation_account_input.text.toString().toLong()
        val categoryId = operation_category_input.text.toString().toLong()
        val visibleInStatistics = operation_visibleInStatistics_input.isChecked
        val date = operation_date_input.text.toString()

        val operation = Operation( userId, accountId, categoryId, title, cost, isExpense, visibleInStatistics, description, date )

        doAsync {
            try {
                val id = database.operationDAO().insertOperation(operation)
                operation.id = id
            } catch (e: Exception){
                runOnUiThread {
                    Toast.makeText(
                            applicationContext,
                            "Wystąpił błąd przy tworzeniu operacji",
                            Toast.LENGTH_LONG).show()
                }
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
            finally {
                val bundle = Bundle()
                bundle.putSerializable("new_operation", operation)
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
