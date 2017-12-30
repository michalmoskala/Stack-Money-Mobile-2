package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.OperationPattern

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

}