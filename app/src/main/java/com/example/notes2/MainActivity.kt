package com.example.notes2

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
    private lateinit var listView: ListView
    private lateinit var btnAddNote: Button
    val NAME_OF_EXTRA : String = "NoteID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        active = true
        listView = findViewById(R.id.listView)
        listView.adapter = MyAdapter(this, MyDB.getNotes())
        btnAddNote = findViewById(R.id.addNote)
        btnAddNote.setOnClickListener {
            val myIntent = Intent(this, OpenNoteActivity::class.java)
            myIntent.putExtra(NAME_OF_EXTRA, -1)
            startActivity(myIntent)
        }
        askForSystemOverlayPermission()
        removeService()
    }

    private fun removeService() {
        run { stopService(Intent(this@MainActivity, FloatingWidgetService::class.java)) }
    }


    override fun onResume() {
        super.onResume()
        active = true
        removeService()
    }

    private fun askForSystemOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 123)
        }
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
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Draw over other app permission not available. Can't start the application without the permission.",
            Toast.LENGTH_LONG
        ).show()
    }

}