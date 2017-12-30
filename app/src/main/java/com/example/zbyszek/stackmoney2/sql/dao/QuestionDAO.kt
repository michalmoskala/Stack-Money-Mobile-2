package com.example.zbyszek.stackmoney2.sql.dao

import android.arch.persistence.room.*
import com.example.zbyszek.stackmoney2.model.Question

@Dao
interface QuestionDAO {

    @Query("SELECT * FROM questions")
    fun getAllQuestions() : List<Question>

    @Query("SELECT * FROM questions WHERE user_id IS :userId")
    fun getAllUserQuestions(userId : Long) : List<Question>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertQuestion(question : Question)

    @Delete()
    fun deleteQuestion(question : Question)
}