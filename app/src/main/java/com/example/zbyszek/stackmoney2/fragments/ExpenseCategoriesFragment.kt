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
import kotlinx.android.synthetic.main.fragment_expense_categories.*
import kotlinx.android.synthetic.main.fragment_expense_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.uiThread

class ExpenseCategoriesFragment : Fragment() {

    lateinit var database : AppDatabase

    private var expenseCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private lateinit var expenseLinearLayoutManager: LinearLayoutManager
    private lateinit var expenseAdapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_expense_categories, container, false)
        databaseConnection()

        doAsync {
            val userId = Preferences.getUserId(activity)
            val sqlCategories = database.categoryDAO().getAllUserBindedCategoriesSQL(userId)
            val expenseCategoriesList = CategoriesHelper.getCategoriesWithSubCategoriesInExpenses(sqlCategories)

            expenseCategoriesArrayList = ArrayList(expenseCategoriesList)

            uiThread {
                expenseLinearLayoutManager = LinearLayoutManager(activity)
                recyclerview_expense_categories.layoutManager = expenseLinearLayoutManager

                expenseAdapter = CategoryListAdapter(expenseCategoriesArrayList)
                recyclerview_expense_categories.adapter = expenseAdapter
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
            this.expenseCategoriesArrayList.add(0, newCategory)
            this.expenseAdapter.notifyItemInserted(0)
        }
    }

    fun receivedNewCategory(newSubCategory: ICategory) {
        activity.runOnUiThread {
            this.expenseCategoriesArrayList[0].subCategories.add(0, newSubCategory)
            this.expenseAdapter.notifyItemChanged(0)
        }
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(activity)
    }

    companion object {
        fun newInstance(): ExpenseCategoriesFragment {
            val fragment = ExpenseCategoriesFragment()
            return fragment
        }
    }
}// Required empty public constructor