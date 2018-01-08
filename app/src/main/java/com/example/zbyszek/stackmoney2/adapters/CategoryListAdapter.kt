package com.example.zbyszek.stackmoney2.adapters

import android.app.Fragment
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.ActivityCompat.startActivityForResult
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
import com.example.zbyszek.stackmoney2.activities.AddCategory
import com.example.zbyszek.stackmoney2.activities.AddOperation
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories
import kotlinx.android.synthetic.main.fragment_category_with_sub_categories_list_row.view.*
import org.jetbrains.anko.textColor

class CategoryListAdapter(private var categoriesList: ArrayList<CategoryWithSubCategories>, var fragment: SuperFragment) : RecyclerView.Adapter<CategoryListAdapter.CategoryHolder>(){

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val itemPhoto = categoriesList[position]
        holder.bind(itemPhoto)
    }

    override fun getItemCount() = categoriesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_with_sub_categories_list_row,null)
        return CategoryHolder(view)
    }

    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var categoryWithSubCategories: CategoryWithSubCategories? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            showPopup(v)
        }

        private fun showPopup(v: View) {
            val popup = PopupMenu(fragment.context, v)
            popup.menuInflater.inflate(R.menu.category_list_item_menu, popup.menu)
            popup.setOnMenuItemClickListener({item -> onMenuItemClick(item)})
            popup.show()
        }

        private fun onMenuItemClick(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.action_edit -> showEditDialog()
                R.id.action_delete -> showDeleteDialog()
                R.id.action_add_sub_category -> showAddSubCategoryDialog()
            }
            return true
        }

        private fun showEditDialog(){
            // TODO: Edit dialog
        }

        private fun showDeleteDialog(){
            MaterialDialog.Builder(fragment.context!!)
                    .title("Usunąć kategorię?")
                    .content("Zostaną również usunięte wszystkie subkategoie")
                    .positiveText("Tak")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which ->
                        fragment.onDialogResult(RequestCodes.DELETE_CATEGORY, ResultCodes.DELETE_OK, categoryWithSubCategories!!.category.id.toString())
                    }
                    .show()
        }

        private fun showAddSubCategoryDialog(){
            val intent = Intent(fragment.context, AddCategory::class.java)
            fragment.startActivityForResult(intent, RequestCodes.ADD)
        }

        fun bind(item: CategoryWithSubCategories) {
            this.categoryWithSubCategories = item
            itemView.name.text = item.category.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.category.color)
            itemView.icon.text = Html.fromHtml(item.category.icon)

            itemView.recyclerview_sub.layoutManager = LinearLayoutManager(itemView.context)
            itemView.recyclerview_sub.adapter = SubCategoryListAdapter(item.subCategories, fragment, this)
        }
    }
}