package com.example.roomdatabase.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    val title: String,

    val desc: String,

    val priority: Int,

    @PrimaryKey(autoGenerate = false)
    val id: Int? = null
)