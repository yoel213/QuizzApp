package com.example.quizzapp

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ThemeDao {
    @Query("SELECT * From Theme")
    fun getAllTheme(): List<Theme>
}