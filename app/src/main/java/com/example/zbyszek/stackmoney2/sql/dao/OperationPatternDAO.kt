package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.operationPattern.BindedOperationPattern
import com.example.zbyszek.stackmoney2.model.operationPattern.OperationPattern

@Dao
interface OperationPatternDAO {

    @Query("SELECT * FROM operation_patterns")
    fun getAllOperationPatterns() : List<OperationPattern>

    @Query("SELECT * FROM operation_patterns WHERE user_id IS :userId")
    fun getAllUserOperationPatterns(userId : Long) : List<OperationPattern>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperationPattern(operationPattern: OperationPattern)

    @Delete()
    fun deleteOperationPattern(operationPattern: OperationPattern)

    @Query("SELECT " +
            "operation_patterns.*, " +
            "Color.value AS color, " +
            "icons.value AS icon, " +
            "AccountColor.value AS account_color, " +
            "CASE WHEN NOT Categories.name THEN Subcategories.name ELSE NULL END sub_category_name, " +
            "CASE WHEN NOT Categories.name THEN Categories.name ELSE Subcategories.name END category_name " +
            "FROM operation_patterns " +
            "JOIN categories Subcategories ON Subcategories.id = operation_patterns.category_id " +
            "LEFT JOIN categories Categories ON Categories.id = Subcategories.parent_category_id " +
            "JOIN colors Color ON Color.id = Subcategories.color_id " +
            "JOIN icons ON icons.id = Subcategories.icon_id " +
            "JOIN accounts ON accounts.id = operation_patterns.account_id " +
            "JOIN colors AccountColor ON AccountColor.id = accounts.color_id " +
            "WHERE operation_patterns.user_id IS :userId " +
            "ORDER BY operation_patterns.id DESC")
    fun getAllUserBindedOperationPatterns(userId : Long) : List<BindedOperationPattern>

    @Query("DELETE FROM operation_patterns WHERE id IS :id AND user_id IS :userId")
    fun deleteOperationPattern(userId: Long, id: Long)
}