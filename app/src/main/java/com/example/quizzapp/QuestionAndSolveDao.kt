package com.example.quizzapp

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.PrimaryKey
import androidx.room.Query
data class QuestionsAndSolvethemre(
    @PrimaryKey @ColumnInfo(name ="question_id") val questionId : Int,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val theme : String
)

@Dao
interface QuestionAndSolveDao {
    @Query("SELECT * From Questions")
    fun getAllQuestions(): List<QuestionsAndSolve>

    @Query("SELECT Questions.question_id , Questions.question, Questions.optionA, Questions.optionB, Questions.optionC, Questions.optionD, theme.theme " +
            "FROM Questions " +
            "JOIN Theme ON Questions.theme_id = theme.theme_id ")
    fun getAllQuestionstheme (): List<QuestionsAndSolvethemre>
}