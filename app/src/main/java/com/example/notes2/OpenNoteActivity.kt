package com.example.notes2

import android.app.Activity
import android.os.Bundle
import com.example.notes2.databinding.ActivityOpenNoteBinding
import com.example.notes2.model.Note

class OpenNoteActivity : Activity() {
    private lateinit var binding: ActivityOpenNoteBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenNoteBinding.inflate(layoutInflater)
        setupViews()
        setContentView(binding.root)

    }

    private fun setupViews() {
        with(binding) {
            val index = intent.getIntExtra(NOTE_KEY, 0)

            if (index == -1) {
                editTitle.hint = getString(R.string.editTitleHint)
                editText.hint = getString(R.string.putNotesHint)
            } else {
                val note: Note = MyDB.getNote(index)
                editTitle.setText(note.title)
                editText.setText(note.text)
            }
            editText.requestFocus()
            buttonOK.setOnClickListener { finish(index) }
        }
    }


    fun finish(index: Int) {
        val note = Note(binding.editTitle.text.toString(), binding.editText.text.toString())
        if (index == -1) {
            MyDB.addNote(note)
        } else {
            MyDB.setNote(index, note)
        }

        super.finish()
    }
    companion object {
        private const val NOTE_KEY = "NoteID"
    }
}