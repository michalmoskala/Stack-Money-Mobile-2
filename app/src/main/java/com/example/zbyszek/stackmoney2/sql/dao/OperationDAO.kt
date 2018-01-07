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

    @Query("SELECT * FROM operations WHERE user_id IS :userId AND date LIKE :month || '-' || :year || '%'")
    fun getAllUserOperationsOfCertainMonth(userId : Long, month: String, year: String) : List<Operation>

    @Query("SELECT operations.*, colors.value AS color, icons.value AS icon " +
            "FROM operations " +
            "JOIN categories ON categories.id = operations.category_id " +
            "JOIN colors ON colors.id = categories.color_id " +
            "JOIN icons ON icons.id = categories.icon_id " +
            "WHERE operations.user_id IS :userId AND date LIKE :month || '-' || :year || '%'")
    fun getAllUserBindedOperationsOfCertainMonth(userId : Long, month: String, year: String) : List<BindedOperation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperation(operation : Operation) : Long

    @Delete()
    fun deleteOperation(operation : Operation)
}