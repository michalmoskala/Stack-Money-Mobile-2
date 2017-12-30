package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.Operation

@Dao
interface OperationDAO {

    @Query("SELECT * FROM operations")
    fun getAllOperations() : List<Operation>

    @Query("SELECT * FROM operations WHERE user_id IS :userId")
    fun getAllUserOperations(userId : Long) : List<Operation>

    @Query("SELECT * FROM operations WHERE user_id IS :userId AND date LIKE :month || '-' || :year || '%'")
    fun getAllUserOperationsOfCertainMonth(userId : Long, month: String, year: String) : List<Operation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperation(operation : Operation)

    @Delete()
    fun deleteOperation(operation : Operation)
}