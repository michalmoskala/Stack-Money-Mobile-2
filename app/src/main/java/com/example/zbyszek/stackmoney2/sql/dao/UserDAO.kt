package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM users")
    fun getAllUsers() : List<User>

    @Query("SELECT login FROM users")
    fun getAllLogins() : List<String>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user : User) : Long

    @Delete()
    fun deleteUser(user : User)

    @Query("SELECT COUNT(*) FROM users WHERE login IS :login AND password IS :password LIMIT 1")
    fun userExists(login : String, password : String) : Boolean

    @Query("SELECT * FROM users WHERE login IS :login AND password IS :password LIMIT 1")
    fun getUser(login : String, password : String) : User?

    @Query("SELECT login FROM users WHERE id IS :id LIMIT 1")
    fun getLoginById(id : Long) : String?

    @Query("SELECT COUNT(*) FROM users WHERE login IS :login LIMIT 1")
    fun userLoginExists(login : String) : Boolean
}