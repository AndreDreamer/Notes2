package com.example.notes2

import OpenNoteActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ListView
import android.widget.Toast

abstract class MainActivity : AppCompatActivity() {

    var active = false
    open abstract var notes: ArrayList<Note>
    open lateinit var db: MyDB
    open lateinit var listView: ListView
    open lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        active = true
        setContentView(R.layout.activity_main)
        context = this
        db = MyDB(this)
        listView = findViewById(R.id.listView)
        askForSystemOverlayPermission()
        refreshNotes()
        findViewById(R.id.addNote).setOnClickListener(View.OnClickListener {
            val myIntent = Intent(context, OpenNoteActivity::class.java)
            myIntent.putExtra("NoteID", -1)
            startActivity(myIntent)
        })

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
        notes = db.getNotes()
        listView.adapter = MyAdapter(this, notes)
    }

    private fun askForSystemOverlayPermission() {

        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 123)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        active = false
        errorToast()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
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