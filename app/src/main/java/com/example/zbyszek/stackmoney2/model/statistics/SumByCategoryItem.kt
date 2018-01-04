package com.example.zbyszek.stackmoney2.model.statistics

import android.arch.persistence.room.ColumnInfo

data class SumByCategoryItem(

        @ColumnInfo(name = "subcategory")
        var subcategory : String,

        @ColumnInfo(name = "category")
        var category : String,

        @ColumnInfo(name = "is_expense")
        var isExpense : Boolean,

        @ColumnInfo(name = "value")
        var value : Double

)