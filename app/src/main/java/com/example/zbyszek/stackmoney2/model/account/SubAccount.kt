package com.example.zbyszek.stackmoney2.model.account

class SubAccount(
        override var id : Long,
        override var userId : Long,
        override var colorId : Int,
        var parentAccountId : Long,
        override var name : String,
        override var color : String = "#2d5ac1"
) : IAccount {
    override fun toAccountSQL(): AccountSQL {
        val category = AccountSQL(userId, colorId, parentAccountId, name)
        category.id = id
        return category
    }
}