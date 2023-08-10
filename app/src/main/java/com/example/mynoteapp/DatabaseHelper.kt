package com.example.mynoteapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_CONTENT TEXT
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert a new note
    fun insertNote(note: Note): Long {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        return writableDatabase.insert(TABLE_NAME, null, values)
    }

    // Update an existing note
    fun updateNote(note: Note): Int {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(note.id.toString())
        return writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)
    }

    // Delete a note
    fun deleteNote(noteId: Long): Int {
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(noteId.toString())
        return writableDatabase.delete(TABLE_NAME, selection, selectionArgs)
    }

    // Retrieve all notes
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = readableDatabase.rawQuery(query, null)
        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                notes.add(Note(id, title, content))
            }
        }
        return notes
    }
}
