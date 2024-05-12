package com.example.lab4

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab4.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        // Spinner for priority selection
        val priorityOptions = arrayOf("Low", "Medium", "High")
        val priorityAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, priorityOptions)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinner.adapter = priorityAdapter

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentText.text.toString()
            val dateTime = getCurrentDateTime()
            // Retrieve selected priority level
            val priority = binding.prioritySpinner.selectedItem.toString()
            val note = Note(0, title, content, dateTime, priority) // Include priority in Note object
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
