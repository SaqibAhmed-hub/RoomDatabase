package com.example.roomdatabase.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomdatabase.data.Note
import com.example.roomdatabase.data.NoteDao
import com.example.roomdatabase.utils.subscribeOnbackground


@Database(entities = [Note::class],version = 1,exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instanse: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            if (instanse == null)
                instanse = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_Database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instanse!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instanse!!)
            }

            private fun populateDatabase(db: NoteDatabase) {
                val noteDao = db.noteDao()
                subscribeOnbackground {
                    noteDao.insert(Note("Movie 1", "MV desription1", 1))
                    noteDao.insert(Note("Movie 2", "MV desription1", 2))
                    noteDao.insert(Note("Movie 3", "MV desription1", 3))
                    noteDao.insert(Note("Movie 4", "MV desription1", 4))
                }
            }
        }
    }
}