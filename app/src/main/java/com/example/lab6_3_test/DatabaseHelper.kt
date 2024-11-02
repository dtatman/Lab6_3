package com.example.lab6_3_test

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NOTES = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NOTES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_CONTENT TEXT,"
                + "$COLUMN_CREATED_AT TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    // Các phương thức CRUD ở đây
    fun addNote(note: Note) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_CREATED_AT, note.createdAt)
        }
        writableDatabase.insert(TABLE_NOTES, null, values)
    }

    fun updateNote(note: Note) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        writableDatabase.update(TABLE_NOTES, values, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
    }

    fun deleteNote(noteId: Long) {
        writableDatabase.delete(TABLE_NOTES, "$COLUMN_ID = ?", arrayOf(noteId.toString()))
    }

    @SuppressLint("Range")
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val cursor = readableDatabase.query(TABLE_NOTES, null, null, null, null, null, null,)
        while (cursor.moveToNext()) {
            notes.add(Note(
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT))
            ))
        }
        cursor.close()
        return notes
    }
}