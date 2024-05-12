package com.example.lab4

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE_TIME = "date_time"
        private const val COLUMN_PRIORITY = "priority"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_DATE_TIME TEXT, $COLUMN_PRIORITY TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE_TIME, getCurrentDateTime())
            put(COLUMN_PRIORITY, note.priority) // Include priority in ContentValues
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllnotes(): List<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME))
            val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))

            val note = Note(id, title, content, dateTime, priority)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_PRIORITY, note.priority) // Update priority as well
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getNoteByID(noteId: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME))
        val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))

        cursor.close()
        db.close()
        return Note(id, title, content, dateTime, priority)
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID= ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
