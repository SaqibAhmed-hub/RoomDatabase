package com.example.roomdatabase.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.roomdatabase.data.Note
import com.example.roomdatabase.data.NoteDao
import com.example.roomdatabase.utils.subscribeOnbackground

class NoteRepository(application: Application) {

    private var noteDao: NoteDao
    private var allNotes: LiveData<List<Note>>

    private val database = NoteDatabase.getInstance(application)

    init {
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    fun insert(note: Note) {
        subscribeOnbackground {
            noteDao.insert(note)
        }
    }

    fun update(note: Note) {
        subscribeOnbackground {
            noteDao.update(note)
        }
    }

    fun delete(note: Note) {
        subscribeOnbackground {
            noteDao.delete(note)
        }
    }

    fun deleteAllNotes() {
        subscribeOnbackground {
            noteDao.deleteAllNotes()
        }
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }


}