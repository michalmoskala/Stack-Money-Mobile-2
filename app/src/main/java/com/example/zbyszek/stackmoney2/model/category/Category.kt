package com.example.zbyszek.stackmoney2.model.category

class Category(
        override var id : Long,
        override var userId : Long,
        override var colorId : Int,
        override var iconId : Int,
        override var visibleInExpenses : Boolean,
        override var visibleInIncomes : Boolean,
        override var name : String
) : ICategory {
}