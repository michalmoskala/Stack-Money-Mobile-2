package com.example.zbyszek.stackmoney2.helpers

class CurrencyHelper {
    companion object {
        fun toZylasy(cost: Int, isExpense: Boolean): String {
            return "%.2f zł".format((if (isExpense) -cost else cost) / 100.0)
        }
    }
}