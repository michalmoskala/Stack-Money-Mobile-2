package com.example.zbyszek.stackmoney2.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddAccount
import com.example.zbyszek.stackmoney2.adapters.AccountListAdapter
import com.example.zbyszek.stackmoney2.helpers.AccountsHelper
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.account.*
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_accounts.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AccountsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AccountsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountsFragment : SuperFragment() {

    lateinit var database : AppDatabase

    private var accountsArrayList: ArrayList<AccountWithSubAccounts> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var accountsAdapter: AccountListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_accounts, container, false)

        view.floatingActionButton_addAccount.setOnClickListener {
            val intent = Intent(context, AddAccount::class.java)
            startActivityForResult(intent, 0)
        }

        databaseConnection()

        val fragment = this
        doAsync {
            val userId = Preferences.getUserId(context!!)
            val sqlAccounts = database.accountDAO().getAllUserBindedAccountsSQL(userId)
            val sqlBalances = database.accountDAO().getAllUserAccountsBalances(userId)
            val accountsList = AccountsHelper.getAccountsWithSubAccounts( sqlAccounts, sqlBalances.associateBy( {it.id}, {it.balance} ) )
            accountsArrayList = ArrayList(accountsList)

            uiThread {
                linearLayoutManager = LinearLayoutManager(activity)
                recyclerview_accounts.layoutManager = linearLayoutManager

                accountsAdapter = AccountListAdapter(accountsArrayList, fragment)
                recyclerview_accounts.adapter = accountsAdapter
            }
        }

        view.floatingActionButton_addAccount.setOnClickListener {
            val intent = Intent(fragment.context, AddAccount::class.java)
            fragment.startActivityForResult(intent, RequestCodes.ADD)
        }

        return view
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when(resultCode) {
            Activity.RESULT_CANCELED -> return
            Activity.RESULT_OK -> {
                val iAccount = data.getSerializableExtra("new_account") as IAccount
                if (iAccount is Account)
                    addAccount(iAccount)
                else if (iAccount is SubAccount)
                    addSubAccount(iAccount)
            }
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: String) {
        super.onDialogResult(requestCode, resultCode, data)

        activity!!.runOnUiThread {
            when(resultCode) {
                ResultCodes.DELETE_OK -> {
                    val id = java.lang.Long.parseLong(data.trim())
                    doAsync {
                        database.accountDAO().deleteAccountSQL(id)
                    }
                    when(requestCode) {
                        RequestCodes.DELETE_CATEGORY -> deleteAccount(id)
                        RequestCodes.DELETE_SUBCATEGORY -> deleteSubAccount(id)
                    }
                }
            }
        }
    }

    private fun addAccount(account: IAccount, initBalance: Long = 0){
            accountsArrayList.add(0, AccountWithSubAccounts(account, initBalance, arrayListOf()))
            accountsAdapter.notifyItemInserted(0)
    }

    private fun addSubAccount(subAccount: SubAccount){
        val index = accountsArrayList.indexOfFirst{it.account.id == subAccount.parentAccountId}
        if (index != -1){
            accountsArrayList[index].subAccounts.add(0, subAccount)
            accountsAdapter.notifyItemChanged(index)
        }
    }

    private fun deleteAccount(id: Long) {
        val index = accountsArrayList.indexOfFirst { it.account.id == id }
        if(index != -1){
            accountsArrayList.removeAt(index)
            accountsAdapter.notifyItemRemoved(index)
        }
    }

    private fun deleteSubAccount(id: Long) {
        accountsArrayList.forEachIndexed lit@ { i, account ->
            val index = account.subAccounts.indexOfFirst { it.id == id }
            if (index != -1) {
                account.subAccounts.removeAt(index)
                accountsAdapter.notifyItemChanged(i)
                return@lit
            }
        }
    }
}// Required empty public constructor
