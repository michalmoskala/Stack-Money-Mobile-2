package com.example.zbyszek.stackmoney2.model

class RequestCodes {
    companion object {
        val DELETE = 1001
        val DELETE_CATEGORY = 1002
        val DELETE_SUBCATEGORY = 1003
        val DELETE_ACCOUNT = 1004
        val DELETE_SUBACCOUNT = 1005
        val ADD = 2001
        val ADD_CATEGORY = 2002
        val ADD_SUBCATEGORY = 2003
        val ADD_SUBACCOUNT = 2004
        val EDIT = 3001
    }
}

//enum class RequestCodes(val req: Int) {
//         DELETE(1001),
//         DELETE_CATEGORY(1002),
//         DELETE_SUBCATEGORY(1003),
//         DELETE_ACCOUNT(1004),
//         DELETE_SUBACCOUNT(1005),
//         ADD(2001),
//         ADD_CATEGORY(2002),
//         ADD_SUBCATEGORY(2003),
//         ADD_SUBACCOUNT(2004),
//         EDIT(3001),
//}