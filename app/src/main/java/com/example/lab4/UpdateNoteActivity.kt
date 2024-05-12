package com.example.lab4

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab4.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class UpdateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.updatetitleEditText.setText(note.title)
        binding.updatecontentText.setText(note.content)

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updatetitleEditText.text.toString()
            val newContent = binding.updatecontentText.text.toString()
            val dateTime = getCurrentDateTime()

            // Retrieve selected priority level
            val newPriority = binding.prioritySpinner.selectedItem.toString()

            val updatedNote = Note(noteId, newTitle, newContent, dateTime, newPriority)
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        }

        // Spinner for priority selection
        val priorityOptions = arrayOf("Low", "Medium", "High")
        val priorityAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, priorityOptions)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinner.adapter = priorityAdapter
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
