package com.example.notes2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes2.databinding.FragmentMainScreenBinding
import com.example.notes2.ui.MyAdapter

class MainScreenFragment : Fragment() {
    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            linearLayoutManager = LinearLayoutManager(activity)
            listView.layoutManager = linearLayoutManager
            listView.adapter = MyAdapter(MyDB.getNotes())
            buttonAddNote.setOnClickListener {
                //CHANGE FRAGMENT

//                val myIntent = Intent(activity, OpenNoteActivity::class.java)
//                myIntent.putExtra(NOTE_KEY, -1)
//                startActivity(myIntent)
            }

            buttonSettings.setOnClickListener {
                throw RuntimeException("Test Crash") // Force a crash for Crashlytics and fun
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.listView.requestFocus()
        binding.listView.adapter?.notifyDataSetChanged()
    }

    companion object {
        private const val NOTE_KEY = "NoteID"
    }
}