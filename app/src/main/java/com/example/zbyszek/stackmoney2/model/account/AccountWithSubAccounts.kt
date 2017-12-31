package com.example.zbyszek.stackmoney2.model.account

class AccountWithSubAccounts(var account: IAccount, var balance : Int, var subAccounts: List<IAccount>)