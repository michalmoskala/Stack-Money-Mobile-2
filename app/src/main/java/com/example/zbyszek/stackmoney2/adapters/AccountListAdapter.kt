package com.example.zbyszek.stackmoney2.adapters

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.model.account.AccountWithSubAccounts
import kotlinx.android.synthetic.main.fragment_account_with_sub_accounts_list_row.view.*
import org.jetbrains.anko.textColor

class AccountListAdapter(private var categoriesList: ArrayList<AccountWithSubAccounts>) : RecyclerView.Adapter<AccountListAdapter.AccountHolder>(){

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val itemPhoto = categoriesList[position]
        holder.bind(itemPhoto)
    }

    override fun getItemCount() = categoriesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_account_with_sub_accounts_list_row,null)
        return AccountHolder(view)
    }

    class AccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var accountWithSubAccounts: AccountWithSubAccounts? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Toast.makeText(itemView.context, accountWithSubAccounts!!.account.name, Toast.LENGTH_SHORT).show()
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }

        fun bind(item: AccountWithSubAccounts) {
            this.accountWithSubAccounts = item
            itemView.name.text = item.account.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.account.color)
            itemView.balance.text = "${"%.2f".format(item.balance / 100.0)} zł"

            itemView.recyclerview_sub_accounts.layoutManager = LinearLayoutManager(itemView.context)
            itemView.recyclerview_sub_accounts.adapter = SubAccountListAdapter(item.subAccounts)
        }
    }
}