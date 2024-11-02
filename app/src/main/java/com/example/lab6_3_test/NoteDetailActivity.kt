package com.example.lab6_3_test

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var note: Note

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        dbHelper = DatabaseHelper(this)
        // Lấy ghi chú từ intent
        val noteId = intent.getLongExtra("NOTE_ID", -1)
        note = dbHelper.getAllNotes().first { it.id == noteId }

        findViewById<EditText>(R.id.edit_note_title).setText(note.title)
        findViewById<EditText>(R.id.edit_note_content).setText(note.content)

        findViewById<Button>(R.id.button_save).setOnClickListener {
            // Cập nhật ghi chú
            val updatedNote = note.copy(
                title = findViewById<EditText>(R.id.edit_note_title).text.toString(),
                content = findViewById<EditText>(R.id.edit_note_content).text.toString(),
            )
            dbHelper.updateNote(updatedNote)
            finish()
        }

        findViewById<Button>(R.id.button_delete).setOnClickListener {
            dbHelper.deleteNote(note.id)
            finish()
        }
    }
}