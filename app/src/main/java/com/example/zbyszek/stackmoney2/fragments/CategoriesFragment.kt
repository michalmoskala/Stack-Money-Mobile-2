package com.example.zbyszek.stackmoney2.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddCategory
import com.example.zbyszek.stackmoney2.adapters.CategoryListAdapter
import com.example.zbyszek.stackmoney2.helpers.CategoriesHelper
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.account.SubCategory
import com.example.zbyszek.stackmoney2.model.category.Category
import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories
import com.example.zbyszek.stackmoney2.model.category.ICategory
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Long.parseLong


class CategoriesFragment : SuperFragment() {

    lateinit var database : AppDatabase

    private var expenseCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private var incomeCategoriesArrayList: ArrayList<CategoryWithSubCategories> = ArrayList()

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
                expenseAdapter = CategoryListAdapter(expenseCategoriesArrayList, fragment)
                incomeAdapter = CategoryListAdapter(incomeCategoriesArrayList, fragment)

                mSectionsPagerAdapter = SectionsPagerAdapter(fragment.activity!!.supportFragmentManager)
                view.tabContainer.adapter = mSectionsPagerAdapter
                view.tabContainer.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view.categoryTabs))
                view.categoryTabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view.tabContainer))
            }
        }

        view.floatingActionButton_addCategory.setOnClickListener {
            val intent = Intent(fragment.context, AddCategory::class.java)
            fragment.startActivityForResult(intent, RequestCodes.ADD)
        }

        return view
    }

    private fun addCategory(category: ICategory){
        if(category.visibleInExpenses){
            expenseCategoriesArrayList.add(0, CategoryWithSubCategories(category, arrayListOf()))
            expenseAdapter.notifyItemInserted(0)
        }
        if(category.visibleInIncomes){
            incomeCategoriesArrayList.add(0, CategoryWithSubCategories(category, arrayListOf()))
            incomeAdapter.notifyItemInserted(0)
        }
    }

    private fun addSubCategory(subCategory: SubCategory){
        if(subCategory.visibleInExpenses){
            val index = expenseCategoriesArrayList.indexOfFirst{it.category.id == subCategory.parentCategoryId}
            if (index != -1){
                expenseCategoriesArrayList[index].subCategories.add(0, subCategory)
                expenseAdapter.notifyItemChanged(index)
            }
        }
        if(subCategory.visibleInIncomes){
            val index = incomeCategoriesArrayList.indexOfFirst{it.category.id == subCategory.parentCategoryId}
            if (index != -1){
                incomeCategoriesArrayList[index].subCategories.add(0, subCategory)
                incomeAdapter.notifyItemChanged(index)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when(resultCode) {
            Activity.RESULT_CANCELED -> return
            Activity.RESULT_OK -> {
                val iCategory = data.getSerializableExtra("new_category") as ICategory
                Toast.makeText(context, iCategory.toString(), Toast.LENGTH_LONG).show()
                if (iCategory is Category)
                    addCategory(iCategory)
                else if (iCategory is SubCategory)
                    addSubCategory(iCategory)
            }
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: String) {
        super.onDialogResult(requestCode, resultCode, data)

        activity!!.runOnUiThread {
            when(resultCode) {
                ResultCodes.DELETE_OK -> {
                    val id = parseLong(data.trim())
                    doAsync {
                        database.categoryDAO().onDeleteCategory(Preferences.getUserId(context!!), id)
                    }
                    when(requestCode) {
                        RequestCodes.DELETE_CATEGORY -> deleteCategory(id)
                        RequestCodes.DELETE_SUBCATEGORY -> deleteSubCategory(id)
                    }
                }
            }
        }
    }

    private fun deleteCategory(id: Long) {
        doAsync {
//            database.categoryDAO().
        }

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
}
