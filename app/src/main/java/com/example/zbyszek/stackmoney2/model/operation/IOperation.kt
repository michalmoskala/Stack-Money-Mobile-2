package com.example.zbyszek.stackmoney2.model.operation

/**
 * Created by Zbyszek on 1/2/2018.
 */

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