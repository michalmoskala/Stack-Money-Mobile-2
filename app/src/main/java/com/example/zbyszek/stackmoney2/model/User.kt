package com.example.zbyszek.stackmoney2.model
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = "users",
        indices = arrayOf(Index(value = "login", unique = true))
)
data class User(
        @ColumnInfo(name = "login")
        var login : String,

        @ColumnInfo(name = "password")
        var password : String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}