package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.category.BindedCategorySQL
import com.example.zbyszek.stackmoney2.model.category.CategorySQL

@Dao
abstract class CategoryDAO {

    @Query("SELECT * FROM categories")
    abstract fun getAllCategoriesSQL() : List<CategorySQL>

    @Query("SELECT * FROM categories WHERE user_id IS :userId")
    abstract fun getAllUserCategoriesSQL(userId : Long) : List<CategorySQL>

    @Query("SELECT categories.*, colors.value AS color, icons.value AS icon " +
            "FROM categories " +
            "JOIN colors ON colors.id = categories.color_id " +
            "JOIN icons ON icons.id = categories.icon_id " +
            "WHERE user_id IS :userId")
    abstract fun getAllUserBindedCategoriesSQL(userId : Long) : List<BindedCategorySQL>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertCategorySQL(categorySQL: CategorySQL) : Long

    @Delete()
    abstract fun deleteCategorySQL(categorySQL: CategorySQL)

    @Query("DELETE FROM categories WHERE id IS :id AND user_id IS :userId")
    abstract fun deleteCategorySQL(userId: Long, id: Long)


    @Query("SELECT * FROM categories WHERE user_id IS :userId AND id IS :id LIMIT 1")
    abstract fun getCategoryById(userId : Long, id: Long): CategorySQL?

    @Query("SELECT * FROM categories WHERE user_id IS :userId AND parent_category_id IS NULL AND id IS NOT :notId LIMIT 1")
    abstract fun defaultCategory(userId : Long, notId: Long): CategorySQL

    @Query("UPDATE operations " +
           "SET category_id = :newId " +
           "WHERE user_id IS :userId AND category_id IN (:oldId, (SELECT DISTINCT id FROM categories WHERE parent_category_id = :oldId))")
    abstract fun changeCategoryIdInOperations(userId : Long, oldId: Long, newId: Long)

    @Query("UPDATE operation_patterns " +
            "SET category_id = :newId " +
            "WHERE user_id IS :userId AND category_id IN (:oldId, (SELECT DISTINCT id FROM categories WHERE parent_category_id = :oldId))")
    abstract fun changeCategoryIdInOperationPatterns(userId : Long, oldId: Long, newId: Long)

    @Transaction
    open fun onDeleteCategory(userId: Long, id: Long) {
        val defaultCategoryId = getCategoryById(userId, id)!!.parentCategoryId
                                ?: defaultCategory(userId, id).id
        changeCategoryIdInOperations(userId, id, defaultCategoryId)
        changeCategoryIdInOperationPatterns(userId, id, defaultCategoryId)
        deleteCategorySQL(userId, id)
    }
}