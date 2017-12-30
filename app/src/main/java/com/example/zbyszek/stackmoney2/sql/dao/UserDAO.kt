package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM users")
    fun getAllUsers() : List<User>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user : User) : Long?

    @Delete()
    fun deleteUser(user : User)

    @Query("SELECT COUNT(*) FROM users WHERE login IS :login AND password IS :password")
    fun userExists(login : String, password : String) : Boolean

    @Query("SELECT COUNT(*) FROM users WHERE login IS :login")
    fun userLoginExists(login : String) : Boolean
}