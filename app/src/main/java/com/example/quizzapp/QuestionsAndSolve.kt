package com.example.quizzapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Questions")
data class QuestionsAndSolve(
    @PrimaryKey @ColumnInfo(name ="question_id") val questionId : Int,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    @ColumnInfo (name = "theme_id") val themeId : Int
)
