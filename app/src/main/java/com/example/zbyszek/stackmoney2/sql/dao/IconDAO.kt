package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.Icon

@Dao
interface IconDAO {

    @Query("SELECT * FROM icons")
    fun getAllIcons(): List<Icon>

    @Query("SELECT * FROM icons WHERE id IS :iconId LIMIT 1")
    fun getIconById(iconId: Int): Icon

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIcon(icon: Icon)

    @Delete()
    fun deleteIcon(icon: Icon)

}