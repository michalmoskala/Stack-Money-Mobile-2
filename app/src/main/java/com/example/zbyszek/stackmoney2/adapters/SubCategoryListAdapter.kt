package com.example.zbyszek.stackmoney2.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.category.ICategory
import kotlinx.android.synthetic.main.fragment_sub_category_list_row.view.*
import org.jetbrains.anko.textColor

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
        }

        override fun onClick(v: View) {

            MaterialDialog.Builder(fragment.context)
                    .title("Usunąć podkategorię?")
//                    .content("Ty stara kurwo zmarnowałaś mi 20 lat życia")
                    .positiveText("Tak")
                    .negativeText("Anuluj")
                    .onPositive{ dialog, which ->
                        fragment.onDialogResult(0,20, categoryHolder.adapterPosition.toString() + " " + adapterPosition.toString())
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, itemCount)
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