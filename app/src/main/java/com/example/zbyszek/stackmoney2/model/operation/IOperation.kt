package com.example.zbyszek.stackmoney2.model.operation



interface IOperation{
    var id:Long
    var userId:Long
    var accountId:Long
    var categoryId:Long
    var title:String
    var cost:Int
    var isExpense:Boolean
    var visibleInStatistics:Boolean
    var description:String
    var date:String

}