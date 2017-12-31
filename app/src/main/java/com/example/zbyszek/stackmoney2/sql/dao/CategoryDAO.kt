package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.category.CategorySQL

@Dao
interface CategoryDAO {

    @Query("SELECT * FROM categories")
    fun getAllCategories() : List<CategorySQL>

    @Query("SELECT * FROM categories WHERE user_id IS :userId")
    fun getAllUserCategories(userId : Long) : List<CategorySQL>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(categorySQL: CategorySQL)

    @Delete()
    fun deleteCategory(categorySQL: CategorySQL)

}