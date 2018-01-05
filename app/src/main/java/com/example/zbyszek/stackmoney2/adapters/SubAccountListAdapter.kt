package com.example.zbyszek.stackmoney2.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.model.account.IAccount
import kotlinx.android.synthetic.main.fragment_sub_account_list_row.view.*
import org.jetbrains.anko.textColor

class SubAccountListAdapter(private var subAccountsArrayList: ArrayList<IAccount>) : RecyclerView.Adapter<SubAccountListAdapter.SubAccountHolder>(){

    override fun onBindViewHolder(holder: SubAccountHolder, position: Int) {
        val subAccount = subAccountsArrayList[position]
        holder.bind(subAccount)
    }

    override fun getItemCount() = subAccountsArrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubAccountHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sub_account_list_row,null)
        return SubAccountHolder(view)
    }

    class SubAccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var subAccount: IAccount? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Toast.makeText(itemView.context, subAccount!!.name, Toast.LENGTH_SHORT).show()
        }

        fun bind(item: IAccount) {
            this.subAccount = item
            itemView.name.text = item.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.color)
        }
    }
}