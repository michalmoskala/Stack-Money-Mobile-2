package com.example.zbyszek.stackmoney2.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddCategory
import com.example.zbyszek.stackmoney2.adapters.AccountListAdapter
import com.example.zbyszek.stackmoney2.helpers.AccountsHelper
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.account.AccountWithSubAccounts
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
            val intent = Intent(fragment.context, AddCategory::class.java)
            fragment.startActivityForResult(intent, RequestCodes.ADD)
        }

        return view
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }
}// Required empty public constructor
