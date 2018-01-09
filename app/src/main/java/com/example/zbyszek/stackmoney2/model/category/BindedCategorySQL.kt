package com.example.zbyszek.stackmoney2.model.category

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import com.example.zbyszek.stackmoney2.model.account.SubCategory

data class BindedCategorySQL(
        @ColumnInfo(name = "user_id")
        var userId : Long,

        @ColumnInfo(name = "color_id")
        var colorId : Int,

        @ColumnInfo(name = "icon_id")
        var iconId : Int,

        @ColumnInfo(name = "color")
        var color : String,

        @ColumnInfo(name = "icon")
        var icon : String,

        @ColumnInfo(name = "parent_category_id")
        var parentCategoryId : Long?,

        @ColumnInfo(name = "visible_in_expenses")
        var visibleInExpenses : Boolean,

        @ColumnInfo(name = "visible_in_incomes")
        var visibleInIncomes : Boolean,

        @ColumnInfo(name = "name")
        var name : String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

    fun convertToCategory(): ICategory {
        return if (parentCategoryId == null)
            Category(id, userId, colorId, iconId, visibleInExpenses, visibleInIncomes, name, color, icon)
        else
            SubCategory(id, userId, colorId, iconId,parentCategoryId ?: 0, visibleInExpenses, visibleInIncomes, name, color, icon)
    }

    fun convertToCategorySQL(): CategorySQL {
        val category = CategorySQL(userId, colorId, iconId, parentCategoryId, visibleInExpenses, visibleInIncomes, name)
        category.id = id
        return category
    }
}