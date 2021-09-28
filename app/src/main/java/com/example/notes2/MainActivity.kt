package com.example.notes2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var active = false
    private lateinit var listView: RecyclerView
    private lateinit var btnAddNote: ImageButton
    val NAME_OF_EXTRA: String = "NoteID"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        linearLayoutManager = LinearLayoutManager(this)
        listView = findViewById(R.id.listView)
        listView.layoutManager = linearLayoutManager

        listView.adapter = MyAdapter(MyDB.getNotes())
        btnAddNote = findViewById(R.id.addNote)
        active = true
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


    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        active = true
        listView.adapter?.notifyDataSetChanged()
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