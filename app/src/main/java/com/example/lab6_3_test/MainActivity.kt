package com.example.lab6_3_test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesList: MutableList<Note>
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        notesList = dbHelper.getAllNotes().toMutableList()

        recyclerView = findViewById(R.id.recycler_view)
        notesAdapter = NotesAdapter(notesList) { note -> showNoteDetail(note) }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notesAdapter

        findViewById<Button>(R.id.button_add_note).setOnClickListener {
            showAddNoteDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAddNoteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.edit_note_title)
        val contentInput = dialogView.findViewById<EditText>(R.id.edit_note_content)

        AlertDialog.Builder(this)
            .setTitle("Thêm Ghi Chú Mới")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val title = titleInput.text.toString()
                val content = contentInput.text.toString()
                val createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val newNote = Note(title = title, content = content, createdAt = createdAt)
                dbHelper.addNote(newNote)
                notesList.clear()
                notesList.addAll(dbHelper.getAllNotes())
                notesAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showNoteDetail(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra("NOTE_ID", note.id)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        notesList.clear()
        notesList.addAll(dbHelper.getAllNotes())
        notesAdapter.notifyDataSetChanged()
    }
}