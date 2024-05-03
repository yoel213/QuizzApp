package com.example.quizzapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [QuestionsAndSolve::class, Theme::class], version = 1)
abstract class QuestionDataBase : RoomDatabase(){
    abstract fun questionandsolverdao() : QuestionAndSolveDao
    abstract fun themedado() : ThemeDao

}