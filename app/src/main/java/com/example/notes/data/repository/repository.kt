package com.example.notes.data.repository

import com.example.notes.data.room.MyDao
import com.example.notes.data.room.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface Repository{

    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
    fun getAllNotes(): Flow<List<Note>>

}

class MyRepository @Inject constructor(
    private val dao: MyDao
):Repository{
    override suspend fun insert(note: Note) {
            dao.insertNote(note = note)
    }

    override suspend fun update(note: Note) {
            dao.updateNote(note = note)
    }

    override suspend fun delete(note: Note) {
            dao.deleteNote(note = note)
    }

    override fun getAllNotes():Flow<List<Note>> {
        return dao.getAllNotes()
            .flowOn(Dispatchers.IO)
            .conflate()
    }

}