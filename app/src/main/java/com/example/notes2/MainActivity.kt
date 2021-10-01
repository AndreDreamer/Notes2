package com.example.notes2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes2.databinding.ActivityMainBinding
import com.example.notes2.ui.MyAdapter

class MainActivity : AppCompatActivity() {


    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupViews()
        setContentView(binding.root)
    }

    private fun setupViews() {
        with(binding) {
            linearLayoutManager = LinearLayoutManager(applicationContext)
            listView.layoutManager = linearLayoutManager
            listView.adapter = MyAdapter(MyDB.getNotes())
            buttonAddNote.setOnClickListener {
                val myIntent = Intent(applicationContext, OpenNoteActivity::class.java)
                myIntent.putExtra(NOTE_KEY, -1)
                startActivity(myIntent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.listView.requestFocus()
        binding.listView.adapter?.notifyDataSetChanged()

    }

    companion object {
        private const val NOTE_KEY = "NoteID"
    }
}