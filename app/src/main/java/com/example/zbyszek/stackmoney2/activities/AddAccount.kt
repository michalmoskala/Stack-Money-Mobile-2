package com.example.zbyszek.stackmoney2.activities

import android.app.Activity
import android.content.Intent
import android.icu.text.DateFormatSymbols
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.account.AccountSQL
import com.example.zbyszek.stackmoney2.model.account.BindedAccountSQL
import com.example.zbyszek.stackmoney2.model.account.IAccount
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import com.example.zbyszek.stackmoney2.sql.dao.AccountDAO
import com.example.zbyszek.stackmoney2.sql.dao.CategoryDAO
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.activity_add_operation.*
import org.jetbrains.anko.doAsync
import org.joda.time.DateTime
import java.util.*

class AddAccount : AppCompatActivity() {

    lateinit var database: AppDatabase
    lateinit var colors: Map<Int, String>
    lateinit var existAccounts: List<AccountSQL>
    lateinit var parentAccounts: List<IAccount>

    lateinit var action: String
    lateinit var editedAccount: AccountSQL

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun onCreateEdit() {
        supportActionBar!!.setTitle(R.string.title_editCategory)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        editedAccount = intent.getSerializableExtra("account") as AccountSQL

        account_name_input.setText(editedAccount.name)
        if (editedAccount.parentAccountId != null)
            account_parentAccount_input.setText(editedAccount.parentAccountId.toString())
        account_colorId_input.setText(editedAccount.colorId.toString())

        button_confirm_new_account.text = getString(R.string.action_update)
        button_confirm_new_account.setOnClickListener {
            editButtonOnClick()
        }
    }

    private fun onCreateSubAccount() {
        supportActionBar!!.setTitle(R.string.title_addCategory)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        button_confirm_new_account.setOnClickListener {
            addButtonOnClick()
        }
    }

    private fun onCreateAddAccount() {
        supportActionBar!!.setTitle(R.string.title_addCategory)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        button_confirm_new_account.setOnClickListener {
            addButtonOnClick()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)
        databaseConnection()
        action = intent.action

        val userId = Preferences.getUserId(applicationContext)
        doAsync {
            colors = database.colorDAO().getAllColors().associateBy({ it.id }, { it.value })
            existAccounts = database.accountDAO().getAllUserAccountsSQL(userId)
            parentAccounts = existAccounts.filter { it.parentAccountId == null }.map { it.convertToAccount() }
        }

        when (action) {
            RequestCodes.EDIT.toString() -> onCreateEdit()
            RequestCodes.ADD_SUBACCOUNT.toString() -> onCreateSubAccount()
            else -> onCreateAddAccount()
        }
    }


    private fun editButtonOnClick() {
        var cancel = false
        var focusView: View? = null

        val name = account_name_input.text.toString().trim()
        val colorId = account_colorId_input.text.toString()
        val parentAccountIdString = account_parentAccount_input.text.toString()
        val parentAccountId = if (parentAccountIdString.isEmpty()) null else parentAccountIdString.toLong()
        val userId = Preferences.getUserId(this)


        if (TextUtils.isEmpty(name)) {
            account_name_input.error = getString(R.string.error_field_required)
            focusView = account_name_input
            cancel = true
        }
        if (parentAccountId == null) {
            if (parentAccounts.any { it.name.toLowerCase() == name.toLowerCase() }) {
                account_name_input.error = "Konto o takiej nazwie już istneje"
                focusView = account_name_input
                cancel = true
            }
        } else {
            if (existAccounts.any { it.parentAccountId == parentAccountId && it.name.toLowerCase() == name.toLowerCase() }) {
                account_name_input.error = "Subkonto o takiej nazwie już istneje"
                focusView = account_name_input
                cancel = true
            }
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true)
//            mAuthTask = UserLoginTask(loginStr, passwordStr)
//            mAuthTask!!.execute(null as Void?)
            val bindedAccountSQL = BindedAccountSQL(userId, colorId.toInt(), parentAccountId, name, colors[colorId.toInt()]!!)
            bindedAccountSQL.id = editedAccount.id
            editCategory(bindedAccountSQL)
        }
    }

    private fun addButtonOnClick() {
        var cancel = false
        var focusView: View? = null

        val name = account_name_input.text.toString().trim()
        val colorId = account_colorId_input.text.toString()
        val parentAccountIdString = account_parentAccount_input.text.toString()
        val parentAccountId = if (parentAccountIdString.isEmpty()) null else parentAccountIdString.toLong()
        val userId = Preferences.getUserId(this)
        val initialValue = account_initialValue_input.text.toString().toInt()

        if (TextUtils.isEmpty(name)) {
            account_name_input.error = getString(R.string.error_field_required)
            focusView = account_name_input
            cancel = true
        }
        if (parentAccountId == null) {
            if (parentAccounts.any { it.name.toLowerCase() == name.toLowerCase() }) {
                account_name_input.error = "Konto o takiej nazwie już istneje"
                focusView = account_name_input
                cancel = true
            }
        } else {
            if (existAccounts.any { it.parentAccountId == parentAccountId && it.name.toLowerCase() == name.toLowerCase() }) {
                account_name_input.error = "Subkonto o takiej nazwie już istneje"
                focusView = account_name_input
                cancel = true
            }
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            doAsync {
                val bindedAccountSQL = BindedAccountSQL(userId, colorId.toInt(), parentAccountId, name, colors[colorId.toInt()]!!)
                addAccount(bindedAccountSQL)

                val category = database.categoryDAO().defaultCategory(userId, -1)


                val dateString = "%04d-%02d-%02d".format(DateTime.now().year(),DateTime.now().monthOfYear,DateTime.now().dayOfMonth())



                val bindedOperation = BindedOperation(
                        userId,
                        bindedAccountSQL.id,
                        category.id,
                        "Wartosc poczatkowa",
                        initialValue,
                        false,
                        true,
                        null,
                        dateString,
                        accountColor = bindedAccountSQL.color,
                        categoryName = category.name,
                        subCategoryName = null,
                        color = bindedAccountSQL.color,
                        icon = database.iconDAO().getIconById(category.iconId).value)
                addOperation(bindedOperation)
            }
        }
    }

    private fun addOperation(bindedOperation: BindedOperation) {
        doAsync {
            try {
                val id = database.operationDAO().insertOperation(bindedOperation.convertToOperation())
                bindedOperation.id = id
                if (operation_create_pattern.isChecked) {
                    database.operationPatternDAO().insertOperationPattern(bindedOperation.convertToOperationPattern())
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                            applicationContext,
                            "Wystąpił błąd przy tworzeniu operacji",
                            Toast.LENGTH_LONG).show()
                }

            }

        }
    }

    private fun addAccount(bindedAccountSQL: BindedAccountSQL) {
        val intent = Intent()
        doAsync {
            try {
                val id = database.accountDAO().insertAccountSQL(bindedAccountSQL.convertToAccountSQL())
                bindedAccountSQL.id = id
            } catch (e: Exception) {
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


    private fun editCategory(bindedAccountSQL: BindedAccountSQL) {
        val intent = Intent()
        doAsync {
            try {
                val categorySQL = bindedAccountSQL.convertToAccountSQL()
                categorySQL.id = bindedAccountSQL.id
                database.accountDAO().updateAccountSQL(categorySQL)
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                            applicationContext,
                            "Wystąpił błąd przy aktualizacji konta",
                            Toast.LENGTH_LONG).show()
                }
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            } finally {
                val bundle = Bundle()
                bundle.putSerializable("edited_account", bindedAccountSQL.convertToAccount())
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }


    private fun databaseConnection() {
        database = AppDatabase.getInMemoryDatabase(this)
    }
}
