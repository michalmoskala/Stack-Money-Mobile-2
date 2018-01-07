package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.account.AccountSQL
import com.example.zbyszek.stackmoney2.model.account.Balance
import com.example.zbyszek.stackmoney2.model.account.BindedAccountSQL

@Dao
interface AccountDAO {

    @Query("SELECT * FROM accounts")
    fun getAllAccountsSQL() : List<AccountSQL>

    @Query("SELECT * FROM accounts WHERE user_id IS :userId")
    fun getAllUserAccountsSQL(userId : Long) : List<AccountSQL>

    @Query("SELECT accounts.*, colors.value AS color " +
            "FROM accounts " +
            "JOIN colors ON colors.id = accounts.color_id " +
            "WHERE user_id IS :userId")
    fun getAllUserBindedAccountsSQL(userId : Long) : List<BindedAccountSQL>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAccountSQL(accountSQL : AccountSQL) : Long

    @Delete()
    fun deleteAccountSQL(accountSQL : AccountSQL)

    @Query("SELECT " +
                "MAIN.id, " +
                "(SELECT TOTAL(CASE WHEN is_expense THEN -cost ELSE cost END) FROM operations WHERE user_id IS :userId AND account_id IN (MAIN.id, (SELECT DISTINCT id FROM accounts WHERE user_id IS :userId AND parent_account_id = MAIN.id)) AND date != '' AND date <= date('now', '+1 day')) AS balance " +
            "FROM accounts MAIN " +
            "WHERE MAIN.user_id IS :userId AND MAIN.parent_account_id IS NULL " +
            "ORDER BY MAIN.name")
    fun getAllUserAccountsBalances(userId : Long) : List<Balance>

}