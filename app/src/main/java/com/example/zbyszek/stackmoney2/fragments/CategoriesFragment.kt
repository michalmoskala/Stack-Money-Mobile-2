package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddCategory
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.adapters.CategoryListAdapter
import com.example.zbyszek.stackmoney2.model.category.*
import com.example.zbyszek.stackmoney2.helpers.CategoriesHelper
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Long.parseLong


class CategoriesFragment : SuperFragment() {

    lateinit var database : AppDatabase

    private var expenseCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private var incomeCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()

    private lateinit var expenseLinearLayoutManager: LinearLayoutManager
    private lateinit var incomeLinearLayoutManager: LinearLayoutManager

    private lateinit var expenseAdapter: CategoryListAdapter
    private lateinit var incomeAdapter: CategoryListAdapter

    private var mSectionsPagerAdapter: CategoriesFragment.SectionsPagerAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        databaseConnection()

        val fragment = this
        doAsync {
            val userId = Preferences.getUserId(context!!)
            val sqlCategories = database.categoryDAO().getAllUserBindedCategoriesSQL(userId)

            val expenseCategoriesList = CategoriesHelper.getCategoriesWithSubCategoriesInExpenses(sqlCategories)
            val incomeCategoriesList = CategoriesHelper.getCategoriesWithSubCategoriesInIncomes(sqlCategories)

            expenseCategoriesArrayList = ArrayList(expenseCategoriesList)
            incomeCategoriesArrayList = ArrayList(incomeCategoriesList)

            uiThread {
                expenseLinearLayoutManager = LinearLayoutManager(fragment.context)
                incomeLinearLayoutManager = LinearLayoutManager(fragment.context)

                expenseAdapter = CategoryListAdapter(expenseCategoriesArrayList, fragment)
                incomeAdapter = CategoryListAdapter(incomeCategoriesArrayList, fragment)

                mSectionsPagerAdapter = SectionsPagerAdapter(activity!!.supportFragmentManager)
                view.tabContainer.adapter = mSectionsPagerAdapter
                view.tabContainer.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view.categoryTabs))
                view.categoryTabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view.tabContainer))
            }
        }

        view.floatingActionButton2.setOnClickListener {
            val category = Category(10,1,3,4,true,true,"Nowe")
            receivedNewCategory(CategoryWithSubCategories(category, arrayListOf()))
        }

        view.floatingActionButton_addCategory.setOnClickListener {
            val intent = Intent(context, AddCategory::class.java)
            startActivityForResult(intent, 0)
        }

        view.floatingActionButton4.setOnClickListener {
            val subCategory = Category(10,1,3,4,true,true,"Nowe")
            receivedNewCategory(subCategory)
        }

        return view
    }

    private fun receivedNewCategory(newCategory: CategoryWithSubCategories) {
        activity!!.runOnUiThread {
            this.expenseCategoriesArrayList.add(0, newCategory)
            this.expenseAdapter.notifyItemInserted(0)
        }
    }

    private fun receivedNewCategory(newSubCategory: ICategory) {
        activity!!.runOnUiThread {
            this.expenseCategoriesArrayList[0].subCategories.add(0, newSubCategory)
            this.expenseAdapter.notifyItemChanged(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val operation = data.getSerializableExtra("new_category")
        activity!!.runOnUiThread {
            Toast.makeText(context, operation.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: String) {
        super.onDialogResult(requestCode, resultCode, data)

        activity!!.runOnUiThread {
            when(resultCode) {
                ResultCodes.DELETE_OK -> {
                    val id = parseLong(data.trim())
                    when(requestCode) {
                        RequestCodes.DELETE_CATEGORY -> deleteCategory(id)
                        RequestCodes.DELETE_SUBCATEGORY -> deleteSubCategory(id)
                    }
                }
            }
        }
    }

    private fun deleteCategory(id: Long) {
        val expenseIndex = expenseCategoriesArrayList.indexOfFirst { it.category.id == id }
        if(expenseIndex != -1){
            expenseCategoriesArrayList.removeAt(expenseIndex)
            expenseAdapter.notifyItemRemoved(expenseIndex)
        }

        val incomeIndex = incomeCategoriesArrayList.indexOfFirst { it.category.id == id }
        if(incomeIndex != -1){
            incomeCategoriesArrayList.removeAt(incomeIndex)
            incomeAdapter.notifyItemRemoved(incomeIndex)
        }
    }

    private fun deleteSubCategory(id: Long) {
        expenseCategoriesArrayList.forEachIndexed lit@ { i, category ->
            val index = category.subCategories.indexOfFirst { it.id == id }
            if (index != -1) {
                category.subCategories.removeAt(index)
                expenseAdapter.notifyItemChanged(i)
                return@lit
            }
        }
        incomeCategoriesArrayList.forEachIndexed lite@ { i, categoryWithSubCategories ->
            val index = categoryWithSubCategories.subCategories.indexOfFirst { it.id == id }
            if (index != -1) {
                categoryWithSubCategories.subCategories.removeAt(index)
                incomeAdapter.notifyItemChanged(i)
                return@lite
            }
        }
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): android.support.v4.app.Fragment {
            if (position == 1)
                return CategoriesRecyclerViewFragment.newInstance(incomeAdapter, LinearLayoutManager(context))
            return CategoriesRecyclerViewFragment.newInstance(expenseAdapter, LinearLayoutManager(context))
        }

        override fun getCount(): Int {
            return 2
        }
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
