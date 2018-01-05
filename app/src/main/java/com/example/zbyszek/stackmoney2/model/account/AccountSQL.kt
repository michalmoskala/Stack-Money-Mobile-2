package com.example.zbyszek.stackmoney2.model.account

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.Color
import com.example.zbyszek.stackmoney2.model.User
import java.io.Serializable

@Entity(
        tableName = "accounts",
        foreignKeys = arrayOf(
                ForeignKey(entity = User::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("user_id"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = Color::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("color_id")),
                ForeignKey(entity = AccountSQL::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("parent_account_id"),
                        onDelete = ForeignKey.CASCADE)
        )
)
data class AccountSQL(
        @ColumnInfo(name = "user_id")
        var userId : Long,

        @ColumnInfo(name = "color_id")
        var colorId : Int,

        @ColumnInfo(name = "parent_account_id")
        var parentAccountId : Long?,

        @ColumnInfo(name = "name")
        var name : String
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

    fun convertToAccount(): IAccount {
        return if (parentAccountId == null)
            Account(id, userId, colorId, name)
        else
            SubAccount(id, userId, colorId, parentAccountId  ?: 0, name)
    }
}