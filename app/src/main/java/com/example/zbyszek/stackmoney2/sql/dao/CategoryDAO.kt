package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.category.BindedCategorySQL
import com.example.zbyszek.stackmoney2.model.category.CategorySQL

@Dao
interface CategoryDAO {

    @Query("SELECT * FROM categories")
    fun getAllCategoriesSQL() : List<CategorySQL>

    @Query("SELECT * FROM categories WHERE user_id IS :userId")
    fun getAllUserCategoriesSQL(userId : Long) : List<CategorySQL>

    @Query("SELECT categories.*, colors.value AS color, icons.value AS icon " +
            "FROM categories " +
            "JOIN colors ON colors.id = categories.color_id " +
            "JOIN icons ON icons.id = categories.icon_id " +
            "WHERE user_id IS :userId")
    fun getAllUserBindedCategoriesSQL(userId : Long) : List<BindedCategorySQL>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategorySQL(categorySQL: CategorySQL)

    @Delete()
    fun deleteCategorySQL(categorySQL: CategorySQL)

}