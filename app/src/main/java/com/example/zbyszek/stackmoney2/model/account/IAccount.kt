package com.example.zbyszek.stackmoney2.model.account

import android.arch.persistence.room.ColumnInfo

/**
 * Created by kumal on 30.12.2017.
 */
interface IAccount {
    var id : Long
    var userId : Long
    var colorId : Int
    var name : String
}