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

//    @Query("SELECT " +
//            "MAIN.id, " +
//            "MAIN.name, " +
//            "(SELECT TOTAL(CASE WHEN is_expense THEN -cost " +
//            "ELSE cost END) " +
//            "FROM operations " +
//            "WHERE user_id IS :userId AND account_id IN (MAIN.id, " +
//            "(SELECT DISTINCT id " +
//            "FROM accounts " +
//            "Where user_id IS :userId AND parent_account_id = MAIN.id)) " +
//            "AND date != '' " +
//            "AND date <= date('now')) / 100.0 AS Balance " +
//            "FROM accounts MAIN " +
//            "WHERE MAIN.user_id IS :userId AND MAIN.parent_account_id IS NULL " +
//            "ORDER BY MAIN.name\n")
//    fun getAllUserAccountsBalances(userId : Long) : List<Long>

}