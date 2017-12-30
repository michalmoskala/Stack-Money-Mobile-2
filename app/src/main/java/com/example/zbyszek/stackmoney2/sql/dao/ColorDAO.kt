package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.Color

@Dao
interface ColorDAO {

    @Query("SELECT * FROM colors")
    fun getAllColors() : List<Color>

    @Query("SELECT * FROM colors WHERE id IS :colorId LIMIT 1")
    fun getColorById(colorId : Int) : Color

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertColor(color : Color)

    @Delete()
    fun deleteColor(color : Color)

}