package com.example.zbyszek.stackmoney2.model.category

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.Color
import com.example.zbyszek.stackmoney2.model.Icon
import com.example.zbyszek.stackmoney2.model.User
import com.example.zbyszek.stackmoney2.model.account.SubCategory

@Entity(
        tableName = "categories",
        foreignKeys = arrayOf(
                ForeignKey(entity = User::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("user_id"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = Color::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("color_id")),
                ForeignKey(entity = Icon::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("icon_id")),
                ForeignKey(entity = CategorySQL::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("parent_category_id"),
                        onDelete = ForeignKey.CASCADE)
        )
)
data class CategorySQL(
        @ColumnInfo(name = "user_id")
        var userId : Long,

        @ColumnInfo(name = "color_id")
        var colorId : Int,

        @ColumnInfo(name = "icon_id")
        var iconId : Int,

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
            Category(id, userId, colorId, iconId, visibleInExpenses, visibleInIncomes, name)
        else
            SubCategory(id, userId, colorId, iconId, parentCategoryId ?: 0, visibleInExpenses, visibleInIncomes, name)
    }
}