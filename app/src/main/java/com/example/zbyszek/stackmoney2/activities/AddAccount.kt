package com.example.zbyszek.stackmoney2.activities

import android.app.Activity
import android.content.Intent
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
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.activity_add_account.*
import org.jetbrains.anko.doAsync
import petrov.kristiyan.colorpicker.ColorPicker
import java.util.ArrayList


class AddAccount : AppCompatActivity() {

    lateinit var database: AppDatabase
    lateinit var colors: Map<Int, String>
    lateinit var existAccounts: List<AccountSQL>
    lateinit var parentAccounts: List<IAccount>

    lateinit var action: String
    lateinit var editedAccount: AccountSQL
    var chosenColorId=3

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
        supportActionBar!!.setTitle(R.string.title_editAccount)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        editedAccount = intent.getSerializableExtra("account") as AccountSQL

        account_name_input.setText(editedAccount.name)
        if (editedAccount.parentAccountId != null)
            account_parentAccount_input.setText(editedAccount.parentAccountId.toString())


        button_confirm_new_account.text = getString(R.string.action_update)
        button_confirm_new_account.setOnClickListener {
            editButtonOnClick()
        }
    }

    private fun onCreateSubAccount() {
        supportActionBar!!.setTitle(R.string.title_addAccount)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        button_confirm_new_account.setOnClickListener {
            addButtonOnClick()
        }
    }

    private fun onCreateAddAccount() {
        supportActionBar!!.setTitle(R.string.title_addAccount)
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

        account_button_colorPicker_input.setOnClickListener {
            val context = this
            doAsync {
                colors = database.colorDAO().getAllColors().associateBy({ it.id }, { it.value })
                val colorsArrayList = (database.colorDAO().getAllColors().map { it.value } as ArrayList<String>)
                runOnUiThread {

                    val colorPicker = ColorPicker(context)
                    colorPicker.setTitle("Wybierz kolor")
                    colorPicker.setRoundColorButton(true)
                    colorPicker.setColors(colorsArrayList)
                            colorPicker.setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                                override fun onChooseColor(position: Int, color: Int) {
                                    if(position>-1)
                                        account_button_colorPicker_input.setBackgroundColor(color)
                                        chosenColorId=position+1
                                }
                                override fun onCancel(){}
                            })


                    colorPicker.show()

                }
            }
        }

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
        val colorId = chosenColorId
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
            editAccount(bindedAccountSQL)
        }
    }

    private fun addButtonOnClick() {
        var cancel = false
        var focusView: View? = null

        val name = account_name_input.text.toString().trim()
        val colorId = chosenColorId
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
            addAccount(bindedAccountSQL)
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


    private fun editAccount(bindedAccountSQL: BindedAccountSQL) {
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
