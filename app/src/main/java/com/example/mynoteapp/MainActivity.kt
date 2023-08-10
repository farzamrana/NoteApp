package com.example.mynoteapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynoteapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        notesAdapter = NotesAdapter(emptyList())

Toast.makeText(this,"WELLCOME TO MY NOTE APP PROJECT",Toast.LENGTH_LONG).show()
        binding.notesRecyclerView.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.addButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            if (title.isNotBlank() && content.isNotBlank()) {
                val note = Note(title = title, content = content)
                val insertedId = databaseHelper.insertNote(note)
                if (insertedId != -1L) {
                    note.id = insertedId
                    notesAdapter.addNote(note)
                    clearFields()
                }
            }
        }
    }

    private fun clearFields() {
        binding.titleEditText.text.clear()
        binding.contentEditText.text.clear()
    }

    override fun onResume() {
        super.onResume()
        val notes = databaseHelper.getAllNotes()
        notesAdapter.setNotes(notes)
    }
}

