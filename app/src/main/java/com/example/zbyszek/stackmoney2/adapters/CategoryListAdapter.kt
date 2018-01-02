package com.example.zbyszek.stackmoney2.adapters

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.jetbrains.anko.startActivityForResult
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddOperation
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories
import kotlinx.android.synthetic.main.fragment_category_with_sub_categories_list_row.view.*
import org.jetbrains.anko.textColor

class CategoryListAdapter(private var categoriesList: ArrayList<CategoryWithSubCategories>, var context: Context) : RecyclerView.Adapter<CategoryListAdapter.CategoryHolder>(){

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val itemPhoto = categoriesList[position]
        holder.bind(itemPhoto)
    }

    override fun getItemCount() = categoriesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_with_sub_categories_list_row,null)
        return CategoryHolder(view, context)
    }

    class CategoryHolder(itemView: View, private val contexty: Context) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var categoryWithSubCategories: CategoryWithSubCategories? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
//            Toast.makeText(itemView.context,categoryWithSubCategories!!.category.name, Toast.LENGTH_SHORT).show()
            val intent = Intent(contexty, AddOperation::class.java)
            (contexty as Fragment).startActivityForResult(intent, 0)
//            startActivityForResult(itemView.context, intent, 0)
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