package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.account.AccountSQL

@Dao
interface AccountDAO {

    @Query("SELECT * FROM accounts")
    fun getAllAccountsSQL() : List<AccountSQL>

    @Query("SELECT * FROM accounts WHERE user_id IS :userId")
    fun getAllUserAccountsSQL(userId : Long) : List<AccountSQL>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAccountSQL(accountSQL : AccountSQL) : Long?

    @Delete()
    fun deleteAccountSQL(accountSQL : AccountSQL)
}