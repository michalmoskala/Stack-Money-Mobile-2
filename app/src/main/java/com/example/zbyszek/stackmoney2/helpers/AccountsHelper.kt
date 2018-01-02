package com.example.zbyszek.stackmoney2.helpers

import com.example.zbyszek.stackmoney2.model.account.AccountWithSubAccounts
import com.example.zbyszek.stackmoney2.model.account.Balance
import com.example.zbyszek.stackmoney2.model.account.BindedAccountSQL
import com.example.zbyszek.stackmoney2.model.category.BindedCategorySQL
import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories

class AccountsHelper {
    companion object {
        fun getAccountsWithSubAccounts(list: List<BindedAccountSQL>, balances: Map<Long, Long>): List<AccountWithSubAccounts> {
            val mainAccounts = list.filter { it.parentAccountId == null }
            val subAccountsGroups = list
                    .filter { it.parentAccountId != null }
                    .groupBy { it.parentAccountId }

            return mainAccounts
                    .map {
                        AccountWithSubAccounts(
                                it.convertToAccount(),
                                balances[it.id] ?: 0,
                                if (subAccountsGroups[it.id] == null) arrayListOf() else ArrayList(subAccountsGroups[it.id]!!.map { it.convertToAccount() }))
                    }
        }
    }
}