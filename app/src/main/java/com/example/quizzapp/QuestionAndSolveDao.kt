package com.example.quizzapp

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionAndSolveDao {
    @Query("SELECT * From Questions")
    fun getAllQuestions(): List<QuestionsAndSolve>
}