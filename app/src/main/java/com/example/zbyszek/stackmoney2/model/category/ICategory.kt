package com.example.zbyszek.stackmoney2.model.category

interface ICategory {
    var id : Long
    var userId : Long
    var colorId : Int
    var iconId : Int
    var visibleInExpenses : Boolean
    var visibleInIncomes : Boolean
    var name : String
}