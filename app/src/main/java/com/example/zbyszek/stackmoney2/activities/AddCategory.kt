package com.example.zbyszek.stackmoney2.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.category.BindedCategorySQL
import com.example.zbyszek.stackmoney2.model.category.CategorySQL
import com.example.zbyszek.stackmoney2.model.category.ICategory
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.activity_add_category.*
import org.jetbrains.anko.doAsync
import petrov.kristiyan.colorpicker.ColorPicker


class AddCategory : AppCompatActivity() {

    lateinit var database : AppDatabase
    lateinit var colors : Map<Int, String>
    lateinit var icons : Map<Int, String>
    lateinit var existCategories : List<CategorySQL>
    lateinit var parentCategories : List<ICategory>
    lateinit var action: String

    lateinit var editedCategory: CategorySQL
    var chosenColorId=3

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onCreateEdit(){
        supportActionBar!!.setTitle(R.string.title_editCategory)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        editedCategory = intent.getSerializableExtra("category") as CategorySQL

        category_name_input.setText(editedCategory.name)
        if (editedCategory.parentCategoryId != null)
            category_parentCategory_input.setText(editedCategory.parentCategoryId.toString())
        category_iconId_input.setText(editedCategory.iconId.toString())
        category_isExpense_input.isChecked = editedCategory.visibleInExpenses
        category_isIncome_input.isChecked = editedCategory.visibleInIncomes

        button_confirm_new_category.text = getString(R.string.action_update)
        button_confirm_new_category.setOnClickListener {
            editButtonOnClick()
        }
    }

    private fun onCreateAddSubCategory(){
        supportActionBar!!.setTitle(R.string.title_addCategory)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        button_confirm_new_category.setOnClickListener {
            addButtonOnClick()
        }
    }

    private fun onCreateAddCategory(){
        supportActionBar!!.setTitle(R.string.title_addCategory)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        button_confirm_new_category.setOnClickListener {
            addButtonOnClick()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)
        databaseConnection()
        action = intent.action
        Toast.makeText(this, action, Toast.LENGTH_SHORT).show()
        val userId = Preferences.getUserId(applicationContext)
        doAsync {
            colors = database.colorDAO().getAllColors().associateBy ( {it.id}, {it.value} )
            icons = database.iconDAO().getAllIcons().associateBy ( {it.id}, {it.value} )
            existCategories = database.categoryDAO().getAllUserCategoriesSQL(userId)
            parentCategories = existCategories.filter { it.parentCategoryId == null }.map { it.convertToCategory() }
        }

       category_button_colorPicker_input.setOnClickListener {
            val context = this
            doAsync {
                colors = database.colorDAO().getAllColors().associateBy({ it.id }, { it.value })
                val colorsArrayList = (database.colorDAO().getAllColors().map { it.value } as ArrayList<String>)
                runOnUiThread {

                    val colorPicker = ColorPicker(context)
                    colorPicker.setTitle("Wybierz kolor")
                    colorPicker.setRoundColorButton(true)
                    colorPicker.setColors(colorsArrayList)
                    colorPicker.setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                        override fun onChooseColor(position: Int, color: Int) {
                            if(position>-1)
                                category_button_colorPicker_input.setBackgroundColor(color)
                            chosenColorId=position+1
                        }
                        override fun onCancel(){}
                    })


                    colorPicker.show()

                }
            }
        }

        when(action){
            RequestCodes.EDIT.toString() -> onCreateEdit()
            RequestCodes.ADD_SUBCATEGORY.toString() -> onCreateAddSubCategory()
            else -> onCreateAddCategory()
        }
    }

    private fun editButtonOnClick(){
        var cancel = false
        var focusView: View? = null

        val colorId = chosenColorId
        val iconId = category_iconId_input.text.toString()
        val name = category_name_input.text.toString().trim()
        val parentCategoryIdString = category_parentCategory_input.text.toString()
        val parentCategoryId = if (parentCategoryIdString.isEmpty()) null else parentCategoryIdString.toLong()
        val userId = Preferences.getUserId(this)
        val visibleInExpenses = category_isExpense_input.isChecked
        val visibleInIncomes = category_isIncome_input.isChecked

        if (TextUtils.isEmpty(name)) {
            category_name_input.error = getString(R.string.error_field_required)
            focusView = category_name_input
            cancel = true
        }
        if (parentCategoryId == null){
            if (parentCategories
                    .filter { it.id != editedCategory.id }
                    .any { it.name.toLowerCase() == name.toLowerCase() }){
                category_name_input.error = "Kategoria o takiej nazwie już istneje"
                focusView = category_name_input
                cancel = true
            }
        }
        else {
            if(existCategories
                    .filter { it.id != editedCategory.id }
                    .any{ it.parentCategoryId == parentCategoryId && it.name.toLowerCase() == name.toLowerCase() }){
                category_name_input.error = "Subkategoria o takiej nazwie już istneje"
                focusView = category_name_input
                cancel = true
            }
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            val bindedCategorySQL = BindedCategorySQL(userId, colorId.toInt(), iconId.toInt(), colors[colorId.toInt()]!!, icons[iconId.toInt()]!!, parentCategoryId,visibleInExpenses,visibleInIncomes,name)
            bindedCategorySQL.id = editedCategory.id
            editCategory(bindedCategorySQL)
        }
    }

    private fun addButtonOnClick(){
        var cancel = false
        var focusView: View? = null

        val colorId = chosenColorId
        val iconId = category_iconId_input.text.toString()
        val name = category_name_input.text.toString().trim()
        val parentCategoryIdString = category_parentCategory_input.text.toString()
        val parentCategoryId = if (parentCategoryIdString.isEmpty()) null else parentCategoryIdString.toLong()
        val userId = Preferences.getUserId(this)
        val visibleInExpenses = category_isExpense_input.isChecked
        val visibleInIncomes = category_isIncome_input.isChecked

        if (TextUtils.isEmpty(name)) {
            category_name_input.error = getString(R.string.error_field_required)
            focusView = category_name_input
            cancel = true
        }
        if (parentCategoryId == null){
            if (parentCategories.any { it.name.toLowerCase() == name.toLowerCase() }){
                category_name_input.error = "Kategoria o takiej nazwie już istneje"
                focusView = category_name_input
                cancel = true
            }
        }
        else {
            if(existCategories.any{ it.parentCategoryId == parentCategoryId && it.name.toLowerCase() == name.toLowerCase() }){
                category_name_input.error = "Subkategoria o takiej nazwie już istneje"
                focusView = category_name_input
                cancel = true
            }
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            val bindedCategorySQL = BindedCategorySQL(userId, colorId.toInt(), iconId.toInt(), colors[colorId.toInt()]!!, icons[iconId.toInt()]!!, parentCategoryId,visibleInExpenses,visibleInIncomes,name)
            addCategory(bindedCategorySQL)
        }
    }

    private fun addCategory(bindedCategorySQL: BindedCategorySQL){
        val intent = Intent()
        doAsync {
            try {
                val id = database.categoryDAO().insertCategorySQL(bindedCategorySQL.convertToCategorySQL())
                bindedCategorySQL.id = id
            } catch (e: Exception){
                runOnUiThread {
                    Toast.makeText(
                            applicationContext,
                            "Wystąpił błąd przy tworzeniu kategorii",
                            Toast.LENGTH_LONG).show()
                }
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
            finally {
                val bundle = Bundle()
                bundle.putSerializable("new_category", bindedCategorySQL.convertToCategory())
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }


    private fun editCategory(bindedCategorySQL: BindedCategorySQL){
        val intent = Intent()
        doAsync {
            try {
                val categorySQL = bindedCategorySQL.convertToCategorySQL()
                categorySQL.id = bindedCategorySQL.id
                database.categoryDAO().updateCategorySQL(categorySQL)
            } catch (e: Exception){
                runOnUiThread {
                    Toast.makeText(
                            applicationContext,
                            "Wystąpił błąd przy aktualizacji kategorii",
                            Toast.LENGTH_LONG).show()
                }
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
            finally {
                val bundle = Bundle()
                bundle.putSerializable("edited_category", bindedCategorySQL.convertToCategory())
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(this)
    }
}
