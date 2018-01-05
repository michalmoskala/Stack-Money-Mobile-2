package com.example.zbyszek.stackmoney2.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.util.SortedListAdapterCallback
import android.support.v7.util.SortedList
import android.text.Html
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.FontManager
import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories
import kotlinx.android.synthetic.main.fragment_category_with_sub_categories_list_row.view.*
import org.jetbrains.anko.textColor
import android.widget.TextView
import android.icu.util.ULocale.getCountry
import android.support.v7.widget.LinearLayoutManager
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.account.SubCategory
import com.example.zbyszek.stackmoney2.model.category.Category
import com.example.zbyszek.stackmoney2.model.category.ICategory
import java.nio.file.Files.size


class CategoryListAdapter(var fragment: SuperFragment) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    internal var list: SortedList<CategoryWithSubCategories>

    init {
        list = SortedList<CategoryWithSubCategories>(CategoryWithSubCategories::class.java, object : SortedList.Callback<CategoryWithSubCategories>() {
            override fun compare(o1: CategoryWithSubCategories, o2: CategoryWithSubCategories): Int {
                return o1.compareTo(o2)
            }
            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }
            override fun areContentsTheSame(oldItem: CategoryWithSubCategories, newItem: CategoryWithSubCategories): Boolean {
                return oldItem.equals(newItem)
            }
            override fun areItemsTheSame(item1: CategoryWithSubCategories, item2: CategoryWithSubCategories): Boolean {
                return item1.equals(item2)
            }
            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }
            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }
            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        })
    }

    //conversation helpers
    fun addAll(countries: List<CategoryWithSubCategories>) {
        list.beginBatchedUpdates()
        for (i in countries.indices) {
            list.add(countries[i])
        }
        list.endBatchedUpdates()
    }

    fun add(countries: CategoryWithSubCategories) {
        list.add(countries)
    }

//    fun add(subCategory: SubCategory){
//        var index = 0
//        while (index < list.size()){
//            if(list[index].category.id != subCategory.parentCategoryId){
//                index++
//                continue
//            }
//            list[index].subCategories.add(subCategory)
//            return
//        }
//    }

//    fun removeWhereId(id: Long) {
//        var result = false
//                .li
//    }

    operator fun get(position: Int): CategoryWithSubCategories {
        return list.get(position)
    }

    fun clear() {
        list.beginBatchedUpdates()
        //remove items at end, to avoid unnecessary array shifting
        while (list.size() > 0) {
            list.removeItemAt(list.size() - 1)
        }
        list.endBatchedUpdates()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_with_sub_categories_list_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = list.get(position)
        holder.bind(country)
    }

    override fun getItemCount(): Int {
        return list.size()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var category: ICategory? = null
        private var subCategories: ArrayList<ICategory>? = null

        init {
            itemView.setOnClickListener(this)
//            fragment.registerForContextMenu(itemView)
        }

        override fun onClick(v: View) {
//            itemView.showContextMenu()
//            showPopup(v)
        }

        fun bind(item: CategoryWithSubCategories) {
            this.category = item.category
            this.subCategories = item.subCategories

            itemView.name.text = item.category.name
            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
            itemView.icon.textColor = Color.parseColor(item.category.color)
            itemView.icon.text = Html.fromHtml(item.category.icon)

            itemView.recyclerview_sub.layoutManager = LinearLayoutManager(itemView.context)
            itemView.recyclerview_sub.adapter = SubCategoryListAdapter(item.subCategories, fragment, this)
        }
    }
}


//
//import android.app.Fragment
//import android.content.Intent
//import android.graphics.Color
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.text.Html
//import android.view.LayoutInflater
//import android.view.MenuItem
//import android.view.View
//import android.view.ViewGroup
//import android.widget.PopupMenu
//import com.afollestad.materialdialogs.MaterialDialog
//import com.example.zbyszek.stackmoney2.R
//import com.example.zbyszek.stackmoney2.activities.AddOperation
//import com.example.zbyszek.stackmoney2.helpers.FontManager
//import com.example.zbyszek.stackmoney2.helpers.SuperFragment
//import com.example.zbyszek.stackmoney2.model.RequestCodes
//import com.example.zbyszek.stackmoney2.model.ResultCodes
//import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories
//import kotlinx.android.synthetic.main.fragment_category_with_sub_categories_list_row.view.*
//import org.jetbrains.anko.textColor
//import android.support.v7.util.SortedList
//import android.support.v7.view.SupportActionModeWrapper
//
//import android.support.v7.widget.util.SortedListAdapterCallback;
//
//
//
//
//
//class CategoryListAdapter : RecyclerView.Adapter<CategoryListAdapter.CategoryHolder> {
//
//    private val sortedList: SortedList<CategoryWithSubCategories>
//    var fragment: SuperFragment
//
//    constructor(categoriesList: ArrayList<CategoryWithSubCategories>, fragment: SuperFragment){
//        this.fragment = fragment
//
//        this.sortedList = SortedList<CategoryWithSubCategories>(CategoryWithSubCategories::class.java,
//                SortedListAdapterCallback<CategoryWithSubCategories>(this) {
//
//            override fun compare(item1: CategoryWithSubCategories?, item2: CategoryWithSubCategories?): Int {
//                return item1!!.compareTo(item2)
//            }
//
//            override fun areContentsTheSame(oldItem: Int?, newItem: Int?): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areItemsTheSame(item1: Int?, item2: Int?): Boolean {
//                return item1!!.toInt() == item2!!.toInt()
//            }
//
//        })
//    }
//
//    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
//        val itemPhoto = categoriesList[position]
//        holder.bind(itemPhoto)
//    }
//
//    override fun getItemCount() = categoriesList.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_with_sub_categories_list_row,null)
//        return CategoryHolder(view)
//    }
//
//    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//
//        private var view: View = itemView
//        private var categoryWithSubCategories: CategoryWithSubCategories? = null
//
//        init {
//            itemView.setOnClickListener(this)
////            fragment.registerForContextMenu(itemView)
//        }
//
//        override fun onClick(v: View) {
////            itemView.showContextMenu()
//            showPopup(v)
//        }
//
//        private fun showPopup(v: View) {
//            val popup = PopupMenu(fragment.context, v)
//            popup.menuInflater.inflate(R.menu.category_list_item_menu, popup.menu)
//            popup.setOnMenuItemClickListener({item -> onMenuItemClick(item)})
//            popup.show()
//        }
//
//        private fun onMenuItemClick(item: MenuItem): Boolean {
//            when(item.itemId){
//                R.id.action_edit -> showEditDialog()
//                R.id.action_delete -> showDeleteDialog()
//                R.id.action_add_sub_category -> showAddSubCategoryDialog()
//            }
//            return true
//        }
//
//        private fun showEditDialog(){
//            MaterialDialog.Builder(fragment.context)
//                    .title("Edycja kategorii")
//                    .positiveText("Edytuj")
//                    .negativeText("Anuluj")
//                    .onPositive{ dialog, which -> }
//                    .show()
//        }
//
//        private fun showDeleteDialog(){
//            MaterialDialog.Builder(fragment.context)
//                    .title("Usunąć kategorię?")
//                    .content("Zostaną również usunięte wszystkie subkategoie")
//                    .positiveText("Tak")
//                    .negativeText("Anuluj")
//                    .onPositive{ dialog, which ->
////                        fragment.onDialogResult(0,10, adapterPosition.toString())
//                        fragment.onDialogResult(RequestCodes.DELETE_CATEGORY, ResultCodes.DELETE_OK, categoryWithSubCategories!!.category.id.toString())
////                        notifyItemRemoved(adapterPosition)
////                        notifyItemRangeChanged(adapterPosition, itemCount)
//                    }
//                    .show()
//        }
//
//        private fun showAddSubCategoryDialog(){
//            MaterialDialog.Builder(fragment.context)
//                    .title("Dodawanie kategorii")
//                    .content("Id głownej kategori: ${categoryWithSubCategories!!.category.id}")
//                    .positiveText("Dodaj")
//                    .negativeText("Anuluj")
//                    .onPositive{ dialog, which -> }
//                    .show()
//        }
//
////        companion object {
////            private val PHOTO_KEY = "PHOTO"
////        }
//
//        fun bind(item: CategoryWithSubCategories) {
//            this.categoryWithSubCategories = item
//            itemView.name.text = item.category.name
//            itemView.icon.typeface = FontManager.getTypeface(itemView.context, FontManager.FONTAWESOME)
//            itemView.icon.textColor = Color.parseColor(item.category.color)
//            itemView.icon.text = Html.fromHtml(item.category.icon)
//
//            itemView.recyclerview_sub.layoutManager = LinearLayoutManager(itemView.context)
//            itemView.recyclerview_sub.adapter = SubCategoryListAdapter(item.subCategories, fragment, this)
//        }
//    }
//}