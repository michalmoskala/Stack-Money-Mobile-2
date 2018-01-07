package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import com.example.zbyszek.stackmoney2.model.operation.Operation

@Dao
interface OperationDAO {

    @Query("SELECT * FROM operations")
    fun getAllOperations() : List<Operation>

    @Query("SELECT * FROM operations WHERE user_id IS :userId")
    fun getAllUserOperations(userId : Long) : List<Operation>

    @Query("SELECT * FROM operations WHERE user_id IS :userId AND date LIKE :year || '-' || :month || '%' AND date <= date('now', '+1 day')")
    fun getAllUserOperationsOfCertainMonth(userId : Long, month: String, year: String) : List<Operation>

//    @Query("SELECT operations.*, colors.value AS color, icons.value AS icon " +
//            "FROM operations " +
//            "JOIN categories ON categories.id = operations.category_id " +
//            "JOIN colors ON colors.id = categories.color_id " +
//            "JOIN icons ON icons.id = categories.icon_id " +
//            "WHERE operations.user_id IS :userId AND date LIKE :year || '-' || :month || '%'")
//    fun getAllUserBindedOperationsOfCertainMonth(userId : Long, month: String, year: String) : List<BindedOperation>

    @Query("SELECT " +
                "operations.*, " +
                "Color.value AS color, " +
                "icons.value AS icon, " +
                "AccountColor.value AS account_color, " +
                "CASE WHEN NOT Categories.name THEN Subcategories.name ELSE NULL END sub_category_name, " +
                "CASE WHEN NOT Categories.name THEN Categories.name ELSE Subcategories.name END category_name " +
            "FROM operations " +
            "JOIN categories Subcategories ON Subcategories.id = operations.category_id " +
            "LEFT JOIN categories Categories ON Categories.id = Subcategories.parent_category_id " +
            "JOIN colors Color ON Color.id = Subcategories.color_id " +
            "JOIN icons ON icons.id = Subcategories.icon_id " +
            "JOIN accounts ON accounts.id = operations.account_id " +
            "JOIN colors AccountColor ON AccountColor.id = accounts.color_id " +
            "WHERE operations.user_id IS :userId AND date LIKE :year || '-' || :month || '%' AND date <= date('now', '+1 day') " +
            "ORDER BY operations.date DESC, operations.id DESC")
    fun getAllUserBindedOperationsOfCertainMonth(userId : Long, month: String, year: String) : List<BindedOperation>

    @Query("SELECT " +
            "operations.*, " +
            "Color.value AS color, " +
            "icons.value AS icon, " +
            "AccountColor.value AS account_color, " +
            "CASE WHEN NOT Categories.name THEN Subcategories.name ELSE NULL END sub_category_name, " +
            "CASE WHEN NOT Categories.name THEN Categories.name ELSE Subcategories.name END category_name " +
            "FROM operations " +
            "JOIN categories Subcategories ON Subcategories.id = operations.category_id " +
            "LEFT JOIN categories Categories ON Categories.id = Subcategories.parent_category_id " +
            "JOIN colors Color ON Color.id = Subcategories.color_id " +
            "JOIN icons ON icons.id = Subcategories.icon_id " +
            "JOIN accounts ON accounts.id = operations.account_id " +
            "JOIN colors AccountColor ON AccountColor.id = accounts.color_id " +
            "WHERE operations.user_id IS :userId AND (date > date('now', '+1 day') OR date IS '' OR date IS NULL)" +
            "ORDER BY operations.date DESC, operations.id DESC")
    fun getAllUserBindedOperationsOfPlanned(userId : Long) : List<BindedOperation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperation(operation : Operation) : Long

    @Delete()
    fun deleteOperation(operation : Operation)

    @Query("DELETE FROM operations WHERE id IS :id")
    fun deleteOperation(id: Long)
}