package com.example.zbyszek.stackmoney2.model.statistics

import android.arch.persistence.room.ColumnInfo

data class SumByCategoryItem(

        @ColumnInfo(name = "subcategory")
        var subcategory : String?,

        @ColumnInfo(name = "category")
        var category : String,

        @ColumnInfo(name = "is_expense")
        var isExpense : Boolean,

        @ColumnInfo(name = "value")
        var value : Int

) {
        fun decimalValue(): Double {
                return value / 100.0
        }

        fun valueToString(): String {
                return "%.2f zł".format(value / 100.0)
        }
}