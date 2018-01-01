package com.example.zbyszek.stackmoney2

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.model.category.*
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.uiThread


class Categories : Fragment() {

    lateinit var database : AppDatabase
    private var categoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_categories, container, false)
        databaseConnection()

        doAsync {
            val sqlCategories = database.categoryDAO().getAllCategories()
            val categoriesList = CategoriesHelper.getCategoriesWithSubCategoriesInExpenses(sqlCategories)
            categoriesArrayList = ArrayList(categoriesList)
            uiThread {
                linearLayoutManager = LinearLayoutManager(activity)
                recyclerview_expense_categories.layoutManager = linearLayoutManager
                adapter = CategoryListAdapter(categoriesArrayList)
                recyclerview_expense_categories.adapter = adapter
            }
        }

        view.floatingActionButton2.setOnClickListener {
            val category = Category(10,1,3,4,true,true,"Nowe")
            receivedNewCategory(CategoryWithSubCategories(category, arrayListOf()))
        }

        view.floatingActionButton4.setOnClickListener {
            val subCategory = Category(10,1,3,4,true,true,"Nowe")
            receivedNewCategory(subCategory)
        }

        return view
    }

    fun receivedNewCategory(newCategory: CategoryWithSubCategories) {
        runOnUiThread {
            this.categoriesArrayList.add(0, newCategory)
            this.adapter.notifyItemInserted(0)
        }
    }

    fun receivedNewCategory(newSubCategory: ICategory) {
        runOnUiThread {
            this.categoriesArrayList[0].subCategories.add(0, newSubCategory)
            this.adapter.notifyItemChanged(0)
        }
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(activity)
    }
}// Required empty public constructor
