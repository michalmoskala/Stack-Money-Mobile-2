package com.example.zbyszek.stackmoney2.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddOperation
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import kotlinx.android.synthetic.main.fragment_operation_list_row.view.*
import org.jetbrains.anko.textColor

class OperationListAdapter(private var operationsList: ArrayList<BindedOperation>, var fragment: SuperFragment) : RecyclerView.Adapter<OperationListAdapter.OperationHolder>(){

    override fun onBindViewHolder(holder: OperationHolder, position: Int) {
        val itemOperation = operationsList[position]
        holder.bind(itemOperation)
    }

    override fun getItemCount() = operationsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_operation_list_row,null)
        return OperationHolder(view)
    }

    inner class OperationHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var operation: BindedOperation? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            showPopup(v)
        }

        private fun showPopup(v: View) {
            val popup = PopupMenu(fragment.context!!, v)
            popup.menuInflater.inflate(R.menu.operation_list_item_menu, popup.menu)
            popup.setOnMenuItemClickListener({item -> onMenuItemClick(item)})
            popup.show()
        }

        private fun onMenuItemClick(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.action_details -> showDetailsDialog()
                R.id.action_edit -> showEditDialog()
                R.id.action_delete -> showDeleteDialog()
            }
            return true
        }

        private fun showDetailsDialog(){
            // TODO: Details dialog
            Toast.makeText(fragment.context, operation.toString(), Toast.LENGTH_LONG).show()
        }

        private fun showEditDialog(){
            val intent = Intent(fragment.context, AddOperation::class.java)
            intent.action = RequestCodes.EDIT.toString()
            val bundle = Bundle()
            bundle.putSerializable("operation", operation!!.convertToOperation())
            intent.putExtras(bundle)
            fragment.startActivityForResult(intent, RequestCodes.EDIT)
        }

        private fun showDeleteDialog(){
            MaterialDialog.Builder(fragment.context!!)
                    .title("Usunąć operację?")
                    .positiveText("Tak")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which ->
                        fragment.onDialogResult(RequestCodes.DELETE, Activity.RESULT_OK, operation!!.id.toString())
                    }
                    .show()
        }

        fun bind(item: BindedOperation) {
            this.operation = item
            itemView.title.text =   if (item.title.isNullOrBlank())
                                        if(item.subCategoryName != null)
                                            item.subCategoryName
                                        else item.categoryName
                                    else item.title

            itemView.category_name.text = item.categoryName
            if(!item.subCategoryName.isNullOrEmpty())
                itemView.sub_category_name.text = item.subCategoryName

            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.color)
            itemView.icon.text = Html.fromHtml(item.icon)

            itemView.cost.text = "%.2f zł".format((if (item.isExpense) -item.cost else item.cost) / 100.0)
        }
    }
}