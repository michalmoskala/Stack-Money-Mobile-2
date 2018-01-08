package com.example.zbyszek.stackmoney2.model.account

import android.arch.persistence.room.ColumnInfo
import java.io.Serializable

/**
 * Created by kumal on 30.12.2017.
 */
interface IAccount : Serializable {
    var id : Long
    var userId : Long
    var colorId : Int
    var name : String
    var color : String

    fun toAccountSQL(): AccountSQL
}