package com.example.zbyszek.stackmoney2.model
import android.arch.persistence.room.*

@Entity(tableName = "questions",
        foreignKeys = arrayOf(
                ForeignKey(entity = User::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("user_id"),
                        onDelete = ForeignKey.CASCADE)
        )
)
data class Question(
        @ColumnInfo(name = "user_id")
        var userId : Long,

        @ColumnInfo(name = "question")
        var question : String,

        @ColumnInfo(name = "answer")
        var answer : String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}