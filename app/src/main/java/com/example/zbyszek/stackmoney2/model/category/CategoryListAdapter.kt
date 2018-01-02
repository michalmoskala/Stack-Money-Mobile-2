package com.example.zbyszek.stackmoney2.model.category

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.model.FontManager
import kotlinx.android.synthetic.main.fragment_category_with_sub_categories_list_row.view.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.textColor

class CategoryListAdapter(private var categoriesList: ArrayList<CategoryWithSubCategories>) : RecyclerView.Adapter<CategoryListAdapter.CategoryHolder>(){

    override fun onBindViewHolder(holder: CategoryListAdapter.CategoryHolder, position: Int) {
        val itemPhoto = categoriesList[position]
        holder.bind(itemPhoto)
    }

    override fun getItemCount() = categoriesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_with_sub_categories_list_row,null)
        return CategoryHolder(view)
    }

    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var categoryWithSubCategories: CategoryWithSubCategories? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Toast.makeText(itemView.context,categoryWithSubCategories!!.category.name, Toast.LENGTH_SHORT).show()
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }

        fun bind(item: CategoryWithSubCategories) {
            this.categoryWithSubCategories = item
            itemView.name.text = item.category.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.category.color)
            itemView.icon.text = Html.fromHtml(item.category.icon)

            itemView.recyclerview_sub.layoutManager = LinearLayoutManager(itemView.context)
            itemView.recyclerview_sub.adapter = SubCategoryListAdapter(item.subCategories)
        }
    }
}