package com.example.zbyszek.stackmoney2.model.account

class AccountWithSubAccounts(var account: IAccount, var balance : Long, var subAccounts: ArrayList<IAccount>)