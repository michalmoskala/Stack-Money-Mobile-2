package com.example.zbyszek.stackmoney2.model.account

class SubAccount(
        override var id : Long,
        override var userId : Long,
        override var colorId : Int,
        var parentAccountId : Long,
        override var name : String
) : IAccount {
}