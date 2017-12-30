package com.example.zbyszek.stackmoney2.model.account

class Account(
        override var id : Long,
        override var userId : Long,
        override var colorId : Int,
        override var name : String
) : IAccount {
}