package com.example.quizzapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Theme")
data class Theme(
    @PrimaryKey @ColumnInfo (name = "theme_id") val themeId : Int,
    val theme :String
)
