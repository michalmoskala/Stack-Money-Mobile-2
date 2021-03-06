package com.example.zbyszek.stackmoney2.model.operation

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.User
import com.example.zbyszek.stackmoney2.model.account.AccountSQL
import com.example.zbyszek.stackmoney2.model.category.CategorySQL
import com.example.zbyszek.stackmoney2.model.operationPattern.OperationPattern
import java.io.Serializable

@Entity(
        tableName = "operations",
        indices = arrayOf(Index(value = "user_id", name = "index_on_user_id_in_operations")),
        foreignKeys = arrayOf(
                ForeignKey(entity = User::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("user_id"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = AccountSQL::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("account_id"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = CategorySQL::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("category_id"))
        )
)
data class Operation(
        @ColumnInfo(name = "user_id")
        var userId : Long,

        @ColumnInfo(name = "account_id")
        var accountId : Long,

        @ColumnInfo(name = "category_id")
        var categoryId : Long,

        @ColumnInfo(name = "title")
        var title : String?,

        @ColumnInfo(name = "cost")
        var cost : Int,

        @ColumnInfo(name = "is_expense")
        var isExpense : Boolean,

        @ColumnInfo(name = "visible_in_statistics")
        var visibleInStatistics : Boolean,

        @ColumnInfo(name = "description")
        var description : String?,

        @ColumnInfo(name = "date")
        var date : String?
): Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

    fun convertToOperationPattern(): OperationPattern {
        return OperationPattern(userId, accountId, categoryId, title, cost, isExpense, visibleInStatistics, description)
    }
}