package com.example.notes.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    var note:String
)