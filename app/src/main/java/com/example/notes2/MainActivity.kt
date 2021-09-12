package com.example.notes2

import OpenNoteActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var active = false
    private lateinit var notes: ArrayList<Note>
    private lateinit var dataBase: MyDB
    private lateinit var listView: ListView
    private lateinit var btnAddNote: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askForSystemOverlayPermission()
        refreshNotes()

        active = true
        dataBase = MyDB(this)
        listView = findViewById(R.id.listView)
        btnAddNote = findViewById(R.id.addNote)
        btnAddNote.setOnClickListener {
            val myIntent = Intent(this, OpenNoteActivity::class.java)
            myIntent.putExtra("NoteID", -1)
            startActivity(myIntent)
        }

        removeService()
    }

    private fun removeService() {
        run { stopService(Intent(this@MainActivity, FloatingWidgetService::class.java)) }
    }


    override fun onResume() {
        super.onResume()
        active = true
        removeService()
        refreshNotes()
    }

    private fun refreshNotes() {
        notes = dataBase.getNotes()
        listView.adapter = MyAdapter(this, notes)
    }

    private fun askForSystemOverlayPermission() {

        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 123)
    }


    override fun onStop() {
        active = false
        errorToast()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123) {
            if (!Settings.canDrawOverlays(this)) {
                errorToast()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Draw over other app permission not available. Can't start the application without the permission.",
            Toast.LENGTH_LONG
        ).show()
    }

}