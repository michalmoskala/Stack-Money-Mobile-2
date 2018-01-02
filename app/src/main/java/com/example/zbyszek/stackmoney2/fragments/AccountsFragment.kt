package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.adapters.AccountListAdapter
import com.example.zbyszek.stackmoney2.helpers.AccountsHelper
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.account.AccountWithSubAccounts
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_accounts.*
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
class AccountsFragment : Fragment() {

    lateinit var database : AppDatabase

    private var accountsArrayList: ArrayList<AccountWithSubAccounts> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var accountsAdapter: AccountListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_accounts, container, false)
        databaseConnection()

        doAsync {
            val userId = Preferences.getUserId(activity)
            val sqlAccounts = database.accountDAO().getAllUserBindedAccountsSQL(userId)
            val sqlBalances = database.accountDAO().getAllUserAccountsBalances(userId)
            val accountsList = AccountsHelper.getAccountsWithSubAccounts( sqlAccounts, sqlBalances.associateBy( {it.id}, {it.balance} ) )
            accountsArrayList = ArrayList(accountsList)

            uiThread {
//                Toast.makeText(activity, "Dalej nie wywaliło się", Toast.LENGTH_SHORT).show()

                linearLayoutManager = LinearLayoutManager(activity)
                recyclerview_accounts.layoutManager = linearLayoutManager

                accountsAdapter = AccountListAdapter(accountsArrayList)
                recyclerview_accounts.adapter = accountsAdapter
            }
        }

        return view
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(activity)
    }
}// Required empty public constructor
