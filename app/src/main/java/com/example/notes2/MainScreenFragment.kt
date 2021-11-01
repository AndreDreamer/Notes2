package com.example.notes2


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
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
                val action = MainScreenFragmentDirections.actionMainScreenFragmentToOpenNoteScreenFragment()
                it.findNavController().navigate(action)
            }

            buttonSettings.setOnClickListener {
                throw RuntimeException("Test Crash") // Force a crash for Crashlytics and fun
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.listView.requestFocus()
        binding.listView.adapter?.notifyDataSetChanged()
    }
}