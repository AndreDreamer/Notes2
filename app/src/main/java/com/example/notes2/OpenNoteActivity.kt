package com.example.notes2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class OpenNoteActivity : Activity() {
    private lateinit var title: EditText
    private lateinit var text: EditText
    private var index = 0
    val NAME_OF_EXTRA : String = "NoteID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.open_note)
        active = true
        title = findViewById(R.id.editTitle)
        text = findViewById(R.id.editText)
        index = intent.getIntExtra(NAME_OF_EXTRA, 0)
        if (index == -1) {
            title.hint = getString(R.string.editTitleHint)
            text.hint = getString(R.string.putNotesHint)
        } else {
            val note: Note = MyDB.getNote(index)
            title.setText(note.title)
            text.setText(note.text)
        }
        val ok = findViewById<Button>(R.id.buttonOK)
        ok.setOnClickListener { finish() }
        removeService()
    }

    private fun removeService() {
        run { stopService(Intent(this@OpenNoteActivity, FloatingWidgetService::class.java)) }
    }

    override fun finish() {

        val note = Note(title.text.toString(), text.text.toString())
        if (index == -1) {
            MyDB.addNote(note)
        } else {
            MyDB.setNote(index, note)
        }

        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
        super.finish()
    }

    override fun onResume() {
        super.onResume()
        active = true
        removeService()
    }

    override fun onStop() {
        super.onStop()
        active = false
        if (!active) {
            // To prevent starting the service if the required permission is NOT granted.
            errorToast()
        }
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Draw over other app permission not available. Can't start the application without the permission.",
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        var active = false
    }
}