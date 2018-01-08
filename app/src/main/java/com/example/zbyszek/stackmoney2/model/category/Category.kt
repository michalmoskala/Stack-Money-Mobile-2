package com.example.zbyszek.stackmoney2.model.category

class Category(
        override var id : Long,
        override var userId : Long,
        override var colorId : Int,
        override var iconId : Int,
        override var visibleInExpenses : Boolean,
        override var visibleInIncomes : Boolean,
        override var name : String,
        override var color : String = "#2d5ac1",
        override var icon : String = "&#xF1B9;"
) : ICategory {
    override fun toCategorySQL(): CategorySQL {
        val category = CategorySQL(userId, colorId, iconId, null, visibleInExpenses, visibleInIncomes, name)
        category.id = id
        return category
    }
}