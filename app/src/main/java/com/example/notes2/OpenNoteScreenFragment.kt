package com.example.notes2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes2.databinding.FragmentOpenNoteScreenBinding
import com.example.notes2.model.Note

class OpenNoteScreenFragment : Fragment() {
    private lateinit var binding: FragmentOpenNoteScreenBinding
    private val args: OpenNoteScreenFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenNoteScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val index = args.noteid

            //check for def value ( new note )
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
            val action =
                OpenNoteScreenFragmentDirections.actionOpenNoteScreenFragmentToMainScreenFragment()
            editTitle.findNavController().navigate(action)


        }

    }

    companion object {
        private const val COUNT_OF_SYMBOL_FOR_CUT = 15
    }
}