package com.example.zbyszek.stackmoney2.model.account

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.example.zbyszek.stackmoney2.model.Color
import com.example.zbyszek.stackmoney2.model.User

data class Balance(
        @ColumnInfo(name = "Balance")
        var balance : Long
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}