package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.util.SortedList
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.adapters.CategoryListAdapter
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.category.*
import com.example.zbyszek.stackmoney2.helpers.CategoriesHelper
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.account.SubCategory
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.uiThread
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.util.regex.Pattern


class CategoriesFragment : SuperFragment() {

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
                expenseLinearLayoutManager = LinearLayoutManager(fragment.context)
                recyclerview_expense_categories.layoutManager = expenseLinearLayoutManager

                incomeLinearLayoutManager = LinearLayoutManager(fragment.context)
                recyclerview_income_categories.layoutManager = incomeLinearLayoutManager

                expenseAdapter = CategoryListAdapter(fragment)
                expenseAdapter.addAll(expenseCategoriesArrayList)
                recyclerview_expense_categories.adapter = expenseAdapter

                incomeAdapter = CategoryListAdapter(fragment)
                incomeAdapter.addAll(incomeCategoriesArrayList)
                recyclerview_income_categories.adapter = incomeAdapter
            }
        }

        view.floatingActionButton2.setOnClickListener {
            val category = Category(10,1,3,4,true,true,"Nowe")
            receivedNewCategory(CategoryWithSubCategories(category, arrayListOf()))
        }

        view.floatingActionButton4.setOnClickListener {
            val subCategory = SubCategory(10,1,3,4,1, true,true,"Nowe")
            receivedNewCategory(subCategory)
        }

        return view
    }

    fun receivedNewCategory(newCategory: CategoryWithSubCategories) {
        runOnUiThread {
            expenseAdapter.add(newCategory)
//            this.expenseCategoriesArrayList.add(0, newCategory)
//            expenseAdapter.notifyDataSetChanged()
//            this.expenseAdapter.notifyItemInserted(0)
        }
    }

    fun receivedNewCategory(newSubCategory: SubCategory) {
        runOnUiThread {
//            expenseAdapter.addCategory(newCategory)
//            expenseCategoriesArrayList.firstOrNull { it.category.id == newSubCategory.parentCategoryId }.subCategories.add(0, newSubCategory)
            this.expenseCategoriesArrayList[0].subCategories.add(0, newSubCategory)
            this.incomeCategoriesArrayList[0].subCategories.add(0, newSubCategory)
//            expenseAdapter.notifyDataSetChanged()
            this.expenseAdapter.notifyItemChanged(0)
            this.incomeAdapter.notifyItemChanged(0)
//            expenseAdapter.add(newSubCategory)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        val operation = data.getSerializableExtra("new_operation")
        runOnUiThread {
            Toast.makeText(this.context, "GITARSON", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: String) {
        super.onDialogResult(requestCode, resultCode, data)

//        expenseCategoriesArrayList.on
        runOnUiThread {
            if (resultCode == ResultCodes.DELETE_OK){
                val index = parseLong(data.trim())
                if (requestCode == RequestCodes.DELETE_CATEGORY){
                    expenseCategoriesArrayList.removeAll { it.category.id == index }
//                    expenseAdapter.notifyDataSetChanged()
                    incomeCategoriesArrayList.removeAll { it.category.id == index }
//                    incomeAdapter.notifyDataSetChanged()
                }
                else if (requestCode == RequestCodes.DELETE_SUBCATEGORY){
                    expenseCategoriesArrayList.forEach{it.subCategories.removeAll { it.id == index }}
//                    expenseAdapter.notifyDataSetChanged()
                    incomeCategoriesArrayList.forEach{it.subCategories.removeAll { it.id == index }}
//                    incomeAdapter.notifyDataSetChanged()
                }
            }
//            if (resultCode == 20){
//                val czo: List<Int> = data.split("\\s+".toRegex()).map { parseInt(it) }
//                expenseCategoriesArrayList[czo[0]].subCategories.removeAt(czo[1])
//            }
//            else if (resultCode == 10){
//                expenseCategoriesArrayList.removeAt(parseInt(data.trim()))
//            }
        }
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(activity)
    }

//    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//
//        val inflater: MenuInflater = activity.menuInflater
//
//        when (v){
//            LayoutInflater.from(context).inflate(R.layout.fragment_sub_category_list_row, null) -> {
//                inflater.inflate(R.menu.sub_category_list_item_menu, menu)
//            }
//            LayoutInflater.from(context).inflate(R.layout.fragment_category_with_sub_categories_list_row, null) -> {
//                inflater.inflate(R.menu.category_list_item_menu, menu)
//            }
//        }
//    }
}// Required empty public constructor
