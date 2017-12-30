package com.example.zbyszek.stackmoney2.model.account

import android.arch.persistence.room.ColumnInfo

/**
 * Created by kumal on 30.12.2017.
 */
interface ICategory {
    var id : Long
    var userId : Long
    var colorId : Int
    var iconId : Int
    var visibleInExpenses : Boolean
    var visibleInIncomes : Boolean
    var name : String
}