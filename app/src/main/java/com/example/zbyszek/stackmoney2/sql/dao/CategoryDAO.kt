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
    fun insertCategorySQL(categorySQL: CategorySQL) : Long

    @Delete()
    fun deleteCategorySQL(categorySQL: CategorySQL)

    @Query("SELECT * FROM categories WHERE user_id IS :userId AND parent_category_id IS NULL LIMIT 1")
    fun defaultCategory(userId : Long): CategorySQL

    @Query("UPDATE operations " +
           "SET category_id = :newId " +
           "WHERE user_id IS :userId AND account_id IN (:oldId, (SELECT DISTINCT id FROM accounts WHERE parent_account_id = :oldId))")
    fun changeCategoryIdInOperations(userId : Long, oldId: Long, newId: Long)

    @Query("UPDATE operation_patterns " +
            "SET category_id = :newId " +
            "WHERE user_id IS :userId AND account_id IN (:oldId, (SELECT DISTINCT id FROM accounts WHERE parent_account_id = :oldId))")
    fun changeCategoryIdInOperationPatterns(userId : Long, oldId: Long, newId: Long)

//    @Transaction
//    open fun onDeleteCategory(userId: Long, id: Long) {
//        val defaultCategory = defaultCategory(userId)
//        changeCategoryIdInOperations(userId, id, defaultCategory.id)
//        changeCategoryIdInOperationPatterns(userId, id, defaultCategory.id)
//        ////        deleteCategorySQL()
//    }
}