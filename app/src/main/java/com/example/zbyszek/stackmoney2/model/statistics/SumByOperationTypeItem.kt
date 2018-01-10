package com.example.zbyszek.stackmoney2.model.statistics

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

data class SumByOperationTypeItem(

        @ColumnInfo(name = "is_expense")
        var isExpense : Boolean,

        @ColumnInfo(name = "value")
        var value : Double

){
        fun decimalValue(): Double {
                return value / 100.0
        }

        fun valueToString(): String {
                return "%.2f zł".format(value / 100.0)
        }
}