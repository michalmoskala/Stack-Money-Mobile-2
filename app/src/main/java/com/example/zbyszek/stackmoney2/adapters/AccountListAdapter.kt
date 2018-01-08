package com.example.zbyszek.stackmoney2.adapters

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.afollestad.materialdialogs.MaterialDialog
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddAccount
import com.example.zbyszek.stackmoney2.activities.AddCategory
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.account.AccountWithSubAccounts
import kotlinx.android.synthetic.main.fragment_account_with_sub_accounts_list_row.view.*
import org.jetbrains.anko.textColor

class AccountListAdapter(private var accountsArrayList: ArrayList<AccountWithSubAccounts>, var fragment: SuperFragment) : RecyclerView.Adapter<AccountListAdapter.AccountHolder>(){

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val itemAccount = accountsArrayList[position]
        holder.bind(itemAccount)
    }

    override fun getItemCount() = accountsArrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_account_with_sub_accounts_list_row,null)
        return AccountHolder(view)
    }

    inner class AccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var accountWithSubAccounts: AccountWithSubAccounts? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            showPopup(v)
        }

        private fun showPopup(v: View) {
            val popup = PopupMenu(fragment.context, v)
            popup.menuInflater.inflate(R.menu.account_list_item_menu, popup.menu)
            popup.setOnMenuItemClickListener({item -> onMenuItemClick(item)})
            popup.show()
        }

        private fun onMenuItemClick(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.action_balance_account -> showBalanceDialog()
                R.id.action_edit -> showEditDialog()
                R.id.action_delete -> showDeleteDialog()
                R.id.action_add_sub_account -> showAddSubAccountDialog()
            }
            return true
        }

        private fun showBalanceDialog(){
            // TODO: Balance dialog
        }

        private fun showEditDialog(){
            // TODO: Edit dialog
        }

        private fun showDeleteDialog(){
            MaterialDialog.Builder(fragment.context!!)
                    .title("Usunąć konto?")
                    .content("Zostaną również usunięte wszystkie subkonta oraz wszystkie przypisane operacje")
                    .positiveText("Tak")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which ->
                        fragment.onDialogResult(RequestCodes.DELETE_CATEGORY, ResultCodes.DELETE_OK, accountWithSubAccounts!!.account.id.toString())
                    }
                    .show()
        }

        private fun showAddSubAccountDialog(){
            val intent = Intent(fragment.context, AddAccount::class.java)
            fragment.startActivityForResult(intent, RequestCodes.ADD)
        }

        fun bind(item: AccountWithSubAccounts) {
            this.accountWithSubAccounts = item
            itemView.name.text = item.account.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.account.color)

            itemView.balance.text = "%.2f zł".format(item.balance / 100.0)

            itemView.recyclerview_sub_accounts.layoutManager = LinearLayoutManager(itemView.context)
            itemView.recyclerview_sub_accounts.adapter = SubAccountListAdapter(item.subAccounts, fragment, this)
        }
    }
}