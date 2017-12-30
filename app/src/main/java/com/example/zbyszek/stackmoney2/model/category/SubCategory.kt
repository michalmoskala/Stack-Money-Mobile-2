package com.example.zbyszek.stackmoney2.model.account

class SubCategory(
        override var id : Long,
        override var userId : Long,
        override var colorId : Int,
        override var iconId : Int,
        var parentCategoryId : Long,
        override var visibleInExpenses : Boolean,
        override var visibleInIncomes : Boolean,
        override var name : String
) : ICategory {
}