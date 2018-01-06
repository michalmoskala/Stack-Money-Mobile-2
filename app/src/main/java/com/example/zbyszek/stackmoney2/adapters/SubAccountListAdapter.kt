package com.example.zbyszek.stackmoney2.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Html
import com.afollestad.materialdialogs.MaterialDialog
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.category.ICategory
import kotlinx.android.synthetic.main.fragment_sub_category_list_row.view.*
import org.jetbrains.anko.textColor
import android.widget.PopupMenu
import android.view.*
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.account.IAccount


class SubAccountListAdapter(private var subAccountsArrayList: ArrayList<IAccount>, var fragment: SuperFragment, var accountHolder: AccountListAdapter.AccountHolder) : RecyclerView.Adapter<SubAccountListAdapter.SubAccountHolder>(){

    override fun onBindViewHolder(holder: SubAccountHolder, position: Int) {
        val subAccount = subAccountsArrayList[position]
        holder.bind(subAccount)
    }

    override fun getItemCount() = subAccountsArrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubAccountHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sub_account_list_row,null)
        return SubAccountHolder(view)
    }

    inner class SubAccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var subAccount: IAccount? = null

        init {
            itemView.setOnClickListener(this)
//            fragment.registerForContextMenu(itemView)
        }

        override fun onClick(v: View) {
//            v.showContextMenu()
            showPopup(v)
        }

        private fun showPopup(v: View) {
            val popup = PopupMenu(fragment.context, v)
            popup.menuInflater.inflate(R.menu.sub_account_list_item_menu, popup.menu)
            popup.setOnMenuItemClickListener({item -> onMenuItemClick(item)})
            popup.show()
        }

        private fun onMenuItemClick(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.action_balance_sub_account -> showBalanceDialog()
                R.id.action_edit -> showEditDialog()
                R.id.action_delete -> showDeleteDialog()
            }
            return true
        }

        private fun showBalanceDialog(){
            MaterialDialog.Builder(fragment.context!!)
                    .title("Wprowadź aktualny stan subkonta")
                    .positiveText("Potwierdź")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which -> }
                    .show()
        }

        private fun showEditDialog(){
            MaterialDialog.Builder(fragment.context!!)
                    .title("Edycja subkonta")
                    .positiveText("Edytuj")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which -> }
                    .show()
        }

        private fun showDeleteDialog(){
            MaterialDialog.Builder(fragment.context!!)
                    .title("Usunąć subkonto?")
                    .positiveText("Tak")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which ->
                        fragment.onDialogResult(RequestCodes.DELETE_SUBCATEGORY, ResultCodes.DELETE_OK, subAccount!!.id.toString())
                    }
                    .show()
        }

        fun bind(item: IAccount) {
            this.subAccount = item
            itemView.name.text = item.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.color)
        }
    }
}

//package com.example.zbyszek.stackmoney2.adapters
//
//import android.graphics.Color
//import android.support.v7.widget.RecyclerView
//import android.text.Html
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import com.example.zbyszek.stackmoney2.R
//import com.example.zbyszek.stackmoney2.helpers.FontManager
//import com.example.zbyszek.stackmoney2.model.account.IAccount
//import kotlinx.android.synthetic.main.fragment_sub_account_list_row.view.*
//import org.jetbrains.anko.textColor
//
//class SubAccountListAdapter(private var subAccountsArrayList: ArrayList<IAccount>) : RecyclerView.Adapter<SubAccountListAdapter.SubAccountHolder>(){
//
//    override fun onBindViewHolder(holder: SubAccountHolder, position: Int) {
//        val subAccount = subAccountsArrayList[position]
//        holder.bind(subAccount)
//    }
//
//    override fun getItemCount() = subAccountsArrayList.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubAccountHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sub_account_list_row,null)
//        return SubAccountHolder(view)
//    }
//
//    class SubAccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//
//        private var view: View = itemView
//        private var subAccount: IAccount? = null
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View) {
//            Toast.makeText(itemView.context, subAccount!!.name, Toast.LENGTH_SHORT).show()
//        }
//
//        fun bind(item: IAccount) {
//            this.subAccount = item
//            itemView.name.text = item.name
//            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
//            itemView.icon.textColor = Color.parseColor(item.color)
//        }
//    }
//}