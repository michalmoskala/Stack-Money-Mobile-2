package com.example.zbyszek.stackmoney2.model.operationPattern

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

data class BindedOperationPattern(
        @ColumnInfo(name = "user_id")
        var userId : Long,

        @ColumnInfo(name = "account_id")
        var accountId : Long,

        @ColumnInfo(name = "category_id")
        var categoryId : Long,

        @ColumnInfo(name = "title")
        var title : String?,

        @ColumnInfo(name = "cost")
        var cost : Int,

        @ColumnInfo(name = "is_expense")
        var isExpense : Boolean,

        @ColumnInfo(name = "visible_in_statistics")
        var visibleInStatistics : Boolean,

        @ColumnInfo(name = "description")
        var description : String?,

        @ColumnInfo(name = "color")
        var color : String,

        @ColumnInfo(name = "icon")
        var icon : String,

        @ColumnInfo(name = "account_color")
        var accountColor : String,

        @ColumnInfo(name = "category_name")
        var categoryName : String,

        @ColumnInfo(name = "sub_category_name")
        var subCategoryName : String?
): Serializable {
        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        var id : Long = 0

        fun convertToOperationPattern(): OperationPattern {
                return OperationPattern(userId, accountId, categoryId, title, cost, isExpense, visibleInStatistics, description)
        }
}