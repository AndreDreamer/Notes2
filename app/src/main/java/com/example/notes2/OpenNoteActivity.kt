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
                editText.hint = getString(R.string.editTitleHint)
            } else {
                val note: Note = MyDB.getNote(index)
                editTitle.setText(note.title)
                editText.setText(note.text)
            }
            editText.requestFocus()
            buttonOK.setOnClickListener { finish(index) }
        }
    }


    private fun finish(index: Int) {
        //check for fields
        with(binding) {
            if (!(editTitle.text.isEmpty() && editText.text.isEmpty())) {
                if (editTitle.text.isEmpty()) {
                    if (editText.text.length > COUNT_OF_SYMBOL_FOR_CUT) {
                        editTitle.setText(
                            editText.text.substring(0, COUNT_OF_SYMBOL_FOR_CUT).plus("...")
                        )
                    } else {
                        editTitle.text = editText.text
                    }
                }
                if (editText.text.isEmpty()) editText.text = editTitle.text
                val note = Note(editTitle.text.toString(), editText.text.toString())
                if (index == -1) {
                    MyDB.addNote(note)
                } else {
                    MyDB.setNote(index, note)
                }
            }
        }
        super.finish()
    }

    companion object {
        private const val NOTE_KEY = "NoteID"
        private const val COUNT_OF_SYMBOL_FOR_CUT = 15
    }
}