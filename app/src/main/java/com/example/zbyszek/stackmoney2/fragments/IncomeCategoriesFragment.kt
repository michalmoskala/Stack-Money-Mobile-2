package com.example.zbyszek.stackmoney2.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.adapters.CategoryListAdapter
import com.example.zbyszek.stackmoney2.helpers.CategoriesHelper
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.category.Category
import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories
import com.example.zbyszek.stackmoney2.model.category.ICategory
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_income_categories.*
import kotlinx.android.synthetic.main.fragment_income_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.uiThread

class IncomeCategoriesFragment : Fragment() {

    lateinit var database : AppDatabase
    private var incomeCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private lateinit var incomeLinearLayoutManager: LinearLayoutManager
    private lateinit var incomeAdapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_income_categories, container, false)
        databaseConnection()

        doAsync {
            val userId = Preferences.getUserId(activity)
            val sqlCategories = database.categoryDAO().getAllUserBindedCategoriesSQL(userId)
            val incomeCategoriesList = CategoriesHelper.getCategoriesWithSubCategoriesInIncomes(sqlCategories)

            incomeCategoriesArrayList = ArrayList(incomeCategoriesList)

            uiThread {
                incomeLinearLayoutManager = LinearLayoutManager(activity)
                recyclerview_income_categories.layoutManager = incomeLinearLayoutManager

                incomeAdapter = CategoryListAdapter(incomeCategoriesArrayList, context)
                recyclerview_income_categories.adapter = incomeAdapter
            }
        }

        view.floatingActionButton2.setOnClickListener {
            val category = Category(10, 1, 3, 4, true, true, "Nowe")
            receivedNewCategory(CategoryWithSubCategories(category, arrayListOf()))
        }

        view.floatingActionButton4.setOnClickListener {
            val subCategory = Category(10, 1, 3, 4, true, true, "Nowe")
            receivedNewCategory(subCategory)
        }

        return view
    }

    fun receivedNewCategory(newCategory: CategoryWithSubCategories) {
        activity.runOnUiThread {
            this.incomeCategoriesArrayList.add(0, newCategory)
            this.incomeAdapter.notifyItemInserted(0)
        }
    }

    fun receivedNewCategory(newSubCategory: ICategory) {
        activity.runOnUiThread {
            this.incomeCategoriesArrayList[0].subCategories.add(0, newSubCategory)
            this.incomeAdapter.notifyItemChanged(0)
        }
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(activity)
    }

    companion object {
        fun newInstance(): IncomeCategoriesFragment {
            val fragment = IncomeCategoriesFragment()
            return fragment
        }
    }
}// Required empty public constructor