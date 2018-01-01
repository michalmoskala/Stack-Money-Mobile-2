package com.example.zbyszek.stackmoney2.model.category

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.model.account.SubCategory
import kotlinx.android.synthetic.main.fragment_sub_category_list_row.view.*

//class SubCategoryListAdapter(var listOfNames: List<ICategory>, context: Context, var listener:(ICategory) -> Unit) : RecyclerView.Adapter<SubCategoryListAdapter.ViewHolder>(){
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(listOfNames[position],listener)
//    }
//
//    override fun getItemCount(): Int = listOfNames.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sub_category_list_row,null)
//        return ViewHolder(view)
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(item: ICategory, listener: (ICategory) -> Unit) = with(itemView) {
//            itemView.name.text = " -> " + item.name
////            itemTitle.text = item.title
////            itemImage.loadUrl(item.url)
//            setOnClickListener {
////                listener(item)
//                Toast.makeText(context,item.name, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}


class SubCategoryListAdapter(private var subCategoriesList: ArrayList<ICategory>) : RecyclerView.Adapter<SubCategoryListAdapter.SubCategoryHolder>(){

    override fun onBindViewHolder(holder: SubCategoryListAdapter.SubCategoryHolder, position: Int) {
        val subCategory = subCategoriesList[position]
        holder.bind(subCategory)
    }

    override fun getItemCount() = subCategoriesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sub_category_list_row,null)
        return SubCategoryHolder(view)
    }

    class SubCategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var subCategory: ICategory? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Toast.makeText(itemView.context,subCategory!!.name, Toast.LENGTH_SHORT).show()
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }

        fun bind(item: ICategory) {
            this.subCategory = item
            itemView.name.text = " -> " + item.name
        }
    }
}