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
import android.widget.Toast
import android.view.*
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes


class SubCategoryListAdapter(private var subCategoriesList: ArrayList<ICategory>, var fragment: SuperFragment, var categoryHolder: CategoryListAdapter.CategoryHolder) : RecyclerView.Adapter<SubCategoryListAdapter.SubCategoryHolder>(){

    override fun onBindViewHolder(holder: SubCategoryHolder, position: Int) {
        val subCategory = subCategoriesList[position]
        holder.bind(subCategory)
    }

    override fun getItemCount() = subCategoriesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sub_category_list_row,null)
        return SubCategoryHolder(view)
    }

    inner class SubCategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var subCategory: ICategory? = null

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
            popup.menuInflater.inflate(R.menu.sub_category_list_item_menu, popup.menu)
            popup.setOnMenuItemClickListener({item -> onMenuItemClick(item)})
            popup.show()
        }

        private fun onMenuItemClick(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.action_edit -> showEditDialog()
                R.id.action_delete -> showDeleteDialog()
            }
            return true
        }

        private fun showEditDialog(){
            MaterialDialog.Builder(fragment.context)
                    .title("Edycja podkategorii")
//                    .content("Ty stara kurwo zmarnowałaś mi 20 lat życia")
                    .positiveText("Edytuj")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which ->
//                        fragment.onDialogResult(0,20, categoryHolder.adapterPosition.toString() + " " + adapterPosition.toString())
//                        notifyItemRemoved(adapterPosition)
//                        notifyItemRangeChanged(adapterPosition, itemCount)
                    }
                    .show()
        }

        private fun showDeleteDialog(){
            MaterialDialog.Builder(fragment.context)
                    .title("Usunąć podkategorię?")
//                    .content("Ty stara kurwo zmarnowałaś mi 20 lat życia")
                    .positiveText("Tak")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which ->
//                        fragment.onDialogResult(0,20, categoryHolder.adapterPosition.toString() + " " + adapterPosition.toString())
                        fragment.onDialogResult(RequestCodes.DELETE_SUBCATEGORY, ResultCodes.DELETE_OK, subCategory!!.id.toString())
//                        notifyItemRemoved(adapterPosition)
//                        notifyItemRangeChanged(adapterPosition, itemCount)
                    }
                    .show()
        }
//        companion object {
//            private val PHOTO_KEY = "PHOTO"
//        }

        fun bind(item: ICategory) {
            this.subCategory = item
            itemView.name.text = item.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.color)
            itemView.icon.text = Html.fromHtml(item.icon)
        }
    }
}