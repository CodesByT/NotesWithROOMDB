package com.example.notes.data.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase: RoomDatabase(){
    abstract val dao: MyDao
}