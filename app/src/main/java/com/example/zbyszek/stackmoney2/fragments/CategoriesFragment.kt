package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.adapters.CategoryListAdapter
import com.example.zbyszek.stackmoney2.model.category.*
import com.example.zbyszek.stackmoney2.helpers.CategoriesHelper
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.uiThread


class CategoriesFragment : Fragment() {

    lateinit var database : AppDatabase

    private var expenseCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private var incomeCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()

    private lateinit var expenseLinearLayoutManager: LinearLayoutManager
    private lateinit var incomeLinearLayoutManager: LinearLayoutManager

    private lateinit var expenseAdapter: CategoryListAdapter
    private lateinit var incomeAdapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_categories, container, false)
        databaseConnection()

        val fragment = this
        doAsync {
            val userId = Preferences.getUserId(context)
            val sqlCategories = database.categoryDAO().getAllUserBindedCategoriesSQL(userId)

            val expenseCategoriesList = CategoriesHelper.getCategoriesWithSubCategoriesInExpenses(sqlCategories)
            val incomeCategoriesList = CategoriesHelper.getCategoriesWithSubCategoriesInIncomes(sqlCategories)

            expenseCategoriesArrayList = ArrayList(expenseCategoriesList)
            incomeCategoriesArrayList = ArrayList(incomeCategoriesList)

            uiThread {
                expenseLinearLayoutManager = LinearLayoutManager(context)
                recyclerview_expense_categories.layoutManager = expenseLinearLayoutManager

                incomeLinearLayoutManager = LinearLayoutManager(context)
                recyclerview_income_categories.layoutManager = incomeLinearLayoutManager

                expenseAdapter = CategoryListAdapter(expenseCategoriesArrayList, fragment)
                recyclerview_expense_categories.adapter = expenseAdapter

                incomeAdapter = CategoryListAdapter(incomeCategoriesArrayList, fragment)
                recyclerview_income_categories.adapter = incomeAdapter
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
            this.expenseCategoriesArrayList.add(0, newCategory)
            this.expenseAdapter.notifyItemInserted(0)
        }
    }

    fun receivedNewCategory(newSubCategory: ICategory) {
        runOnUiThread {
            this.expenseCategoriesArrayList[0].subCategories.add(0, newSubCategory)
            this.expenseAdapter.notifyItemChanged(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        val operation = data.getSerializableExtra("new_operation")
        runOnUiThread {
            Toast.makeText(this.context, "GITARSON", Toast.LENGTH_SHORT).show()
        }
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(activity)
    }
}// Required empty public constructor
