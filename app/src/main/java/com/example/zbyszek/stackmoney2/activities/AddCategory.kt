package com.example.zbyszek.stackmoney2.activities

import android.content.Intent
import android.os.Bundle
import com.example.zbyszek.stackmoney2.R
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_operation.*
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.category.CategorySQL
import kotlinx.android.synthetic.main.activity_add_category.*


class AddCategory() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)


        button_confirm_new_category.setOnClickListener {
            val intent = Intent()

            val colorId = category_colorId_input.text.toString().toInt()
            val iconId = category_iconId_input.text.toString().toInt()
            val name = category_name_input.text.toString()
            val parentCategoryId = category_parentCategory_input.text.toString().toLong()
            val userId = Preferences.getUserId(this)
            val visibleInExpenses = category_isExpense_input.isChecked
            val visibleInIncomes = category_isExpense_input.isChecked

            val category = CategorySQL(userId,colorId,iconId,parentCategoryId,visibleInExpenses,visibleInIncomes,name)
            val bundle = Bundle()
            bundle.putSerializable("new_category", category)

            intent.putExtras(bundle)
            setResult(0, intent)
            finish()


        }
    }
}
