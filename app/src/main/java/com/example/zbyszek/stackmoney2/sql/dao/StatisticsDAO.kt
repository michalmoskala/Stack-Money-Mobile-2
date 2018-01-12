package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.operation.Operation
import com.example.zbyszek.stackmoney2.model.statistics.SumByCategoryItem
import com.example.zbyszek.stackmoney2.model.statistics.SumByOperationTypeItem

@Dao
interface StatisticsDAO {

    @Query("SELECT is_expense, SUM(cost) AS value " +
            "FROM operations " +
            "WHERE user_id IS :userId AND visible_in_statistics AND date BETWEEN :startDate AND :endDate GROUP BY is_expense")
    fun getSumsByOperationType(userId: Long, startDate: String, endDate: String) : List<SumByOperationTypeItem>

    @Query("SELECT " +
            "CASE WHEN NOT Categories.name THEN Subcategories.name ELSE NULL END subcategory, " +
            "CASE WHEN NOT Categories.name THEN Categories.name ELSE Subcategories.name END category, " +
            "SUM(cost) value, " +
            "Operations.is_expense is_expense " +
            "FROM operations Operations " +
            "JOIN categories Subcategories ON Subcategories.id = Operations.category_id " +
            "LEFT JOIN categories Categories ON Categories.id = Subcategories.parent_category_id " +
            "WHERE Operations.user_id IS :userId AND Operations.visible_in_statistics AND Operations.date BETWEEN :startDate AND :endDate " +
            "GROUP BY is_expense, category_id " +
            "ORDER BY is_expense, category, value DESC")
    fun getSumsByCategory(userId : Long, startDate: String, endDate: String) : List<SumByCategoryItem>

}