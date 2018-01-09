package com.example.zbyszek.stackmoney2.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.fragments.DatePickerFragment
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.account.BindedAccountSQL
import com.example.zbyszek.stackmoney2.model.category.BindedCategorySQL
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import com.example.zbyszek.stackmoney2.model.operation.Operation
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.activity_add_operation.*
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.padding
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import java.lang.Math.round
import java.util.*
import android.widget.ArrayAdapter
import com.example.zbyszek.stackmoney2.model.SpinnerItem


class AddOperation : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var database : AppDatabase
    lateinit var colors : Map<Int, String>
    lateinit var icons : Map<Int, String>
    lateinit var existCategories : List<BindedCategorySQL>
    lateinit var existAccounts : List<BindedAccountSQL>

    lateinit var action: String
    lateinit var editedOperation: Operation

    lateinit var datePicker: DatePickerDialog

    lateinit var expenseCategoriesAdapter: ArrayAdapter<SpinnerItem>
    lateinit var incomeCategoriesAdapter: ArrayAdapter<SpinnerItem>
    lateinit var accountsAdapter: ArrayAdapter<SpinnerItem>

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val day = datePicker.datePicker.dayOfMonth
        val month = datePicker.datePicker.month + 1
        val year = datePicker.datePicker.year
        val cal = DateTime(year, month, day, 0, 0)
        operation_date_input.setText(cal.toString("d MMMM YYYY", Locale.forLanguageTag("pl-PL")))
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


    private fun onCreateEdit(){
        supportActionBar!!.setTitle(R.string.title_editOperation)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        editedOperation = intent.getSerializableExtra("operation") as Operation

        operation_name_input.setText(editedOperation.title)
//        operation_account_input.setText(editedOperation.accountId.toString())
//        operation_category_input.setText(editedOperation.categoryId.toString())
        operation_amount_input.setText(editedOperation.cost.toString())
        operation_description_input.setText(editedOperation.description ?: "")
        operation_visibleInStatistics_input.isChecked = editedOperation.visibleInStatistics
        operation_radio_isIncome.isChecked = !editedOperation.isExpense
        operation_date_input.setText(editedOperation.date ?: "")

        operation_button_confirm_new_operation.text = getString(R.string.action_update)
        operation_button_confirm_new_operation.setOnClickListener {
            editButtonOnClick()
        }

        operation_create_pattern.visibility = View.GONE
    }

    private fun onCreateAdd(){
        supportActionBar!!.setTitle(R.string.title_addOperation)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        operation_button_confirm_new_operation.setOnClickListener {
            addButtonOnClick()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_operation)
        databaseConnection()
        action = intent.action

        val dateNow = DateTime.now()
        datePicker = DatePickerDialog(this, this, dateNow.year, dateNow.monthOfYear - 1, dateNow.dayOfMonth)
        operation_date_input.setOnClickListener {
            datePicker.show()
        }

        val userId = Preferences.getUserId(applicationContext)
        doAsync {
            colors = database.colorDAO().getAllColors().associateBy ( {it.id}, {it.value} )
            icons = database.iconDAO().getAllIcons().associateBy ( {it.id}, {it.value} )
            existCategories = database.categoryDAO().getAllUserBindedCategoriesSQL(userId)
            existAccounts = database.accountDAO().getAllUserBindedAccountsSQL(userId)

            expenseCategoriesAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    existCategories.filter { it.visibleInExpenses }.map { SpinnerItem(it.name, it.id) })

            incomeCategoriesAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    existCategories.filter { it.visibleInIncomes }.map { SpinnerItem(it.name, it.id) })

            accountsAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    existAccounts.map { SpinnerItem(it.name, it.id) })

            uiThread {
                expenseCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                incomeCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                operation_spinner_category.adapter = expenseCategoriesAdapter
                operation_spinner_account.adapter = accountsAdapter

                operation_radio_isExpense.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked)
                        operation_spinner_category.adapter = expenseCategoriesAdapter
                }
                operation_radio_isIncome.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked)
                        operation_spinner_category.adapter = incomeCategoriesAdapter
                }

                when(action){
                    RequestCodes.EDIT.toString() -> onCreateEdit()
                    else -> onCreateAdd()
                }
            }
        }
    }

    fun showTimePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "timePicker")
    }

    private fun validateOperation(): BindedOperation? {
        var cancel = false
        var focusView: View? = null

        val userId = Preferences.getUserId(this)
        val title = operation_name_input.text.toString().trim()
        val cost =  if(operation_amount_input.currencyText.isNullOrEmpty()) 0
        else round(operation_amount_input.currencyDouble * 100.0).toInt()
        val isExpense = operation_radio_isExpense.isChecked
        val description = operation_description_input.text.toString()
        val accountId = (operation_spinner_account.selectedItem as SpinnerItem).tag
        val categoryId = (operation_spinner_category.selectedItem as SpinnerItem).tag//operation_category_input.text.toString()
        val visibleInStatistics = operation_visibleInStatistics_input.isChecked
//        val date = operation_date_input.text.toString()

        val datePickerf = datePicker.datePicker
        val date = "%04d-%02d-%02d".format(datePickerf.year,datePickerf.month+1,datePickerf.dayOfMonth)

        if(cost == 0){
            operation_amount_input.error = "Wartość nie może być zerowa"//getString()
            focusView = operation_amount_input
            cancel = true
        }

//        if (TextUtils.isEmpty(accountId)) {
//            operation_account_input.error = getString(R.string.error_field_required)
//            focusView = operation_account_input
//            cancel = true
//        }

//        if (TextUtils.isEmpty(categoryId)) {
//            operation_category_input.error = getString(R.string.error_field_required)
//            focusView = operation_category_input
//            cancel = true
//        }

        if (cancel) {
            focusView?.requestFocus()
            return null
        } else {
            val category = existCategories.firstOrNull { it.id == categoryId.toLong() && it.parentCategoryId == null }
                    ?: existCategories.first { it.id == existCategories.first { it.id == categoryId.toLong() }.parentCategoryId }

            val subCategory = existCategories.firstOrNull { it.id == categoryId.toLong() && it.parentCategoryId != null }

            val operation = BindedOperation(
                    userId,
                    accountId.toLong(),
                    categoryId,//.toLong(),
                    title,
                    cost,
                    isExpense,
                    visibleInStatistics,
                    description,
                    date,
                    accountColor = existAccounts.first { it.id == accountId.toLong() }.color,
                    categoryName = category.name,
                    subCategoryName = subCategory?.name,
                    color = subCategory?.color ?: category.color,
                    icon = subCategory?.icon ?: category.icon)
            return operation
        }
    }

    private fun addButtonOnClick() {
        val operation = validateOperation()
        if (operation != null)
            addOperation(operation)
    }

    private fun addOperation(bindedOperation: BindedOperation){
        val intent = Intent()
        doAsync {
            try {
                val id = database.operationDAO().insertOperation(bindedOperation.convertToOperation())
                bindedOperation.id = id
                if(operation_create_pattern.isChecked){
                    database.operationPatternDAO().insertOperationPattern(bindedOperation.convertToOperationPattern())
                }
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
                bundle.putSerializable("new_operation", bindedOperation)
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun editButtonOnClick() {
        val operation = validateOperation()
        if (operation != null){
            operation.id = editedOperation.id
            editOperation(operation)
        }
    }

    private fun editOperation(bindedOperation: BindedOperation){
        val intent = Intent()
        doAsync {
            try {
                val operation = bindedOperation.convertToOperation()
                database.operationDAO().updateOperation(operation)
            } catch (e: Exception){
                runOnUiThread {
                    Toast.makeText(
                            applicationContext,
                            "Wystąpił błąd przy edycji operacji",
                            Toast.LENGTH_LONG).show()
                }
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
            finally {
                val bundle = Bundle()
                bundle.putSerializable("edited_operation", bindedOperation)
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
