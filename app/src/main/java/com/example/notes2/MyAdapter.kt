package com.example.notes2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MyAdapter(private val context: Activity, private val notes: ArrayList<Note>) :
    ArrayAdapter<Note>(context, R.layout.list_item, notes) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View = if (convertView == null) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            inflater.inflate(R.layout.list_item, parent, false)
        } else {
            convertView
        }

        val title = view.findViewById<TextView>(R.id.title)
        val text = view.findViewById<TextView>(R.id.text)
        val button = view.findViewById<Button>(R.id.button)

        title.text = notes[position].title
        text.text = notes[position].text

        //delete btn Event
        button.setOnClickListener {
            val dropDownMenu = PopupMenu(context, button)
            dropDownMenu.menuInflater.inflate(R.menu.drop_down_menu, dropDownMenu.menu)
            dropDownMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "delete") {
                    MyDB.removeNote(position)
                    notifyDataSetChanged()
                }
                if (menuItem.title == "info") {
                    //todo info menu
                }
                true
            }
            dropDownMenu.show()
        }

        //note click event
        view.setOnClickListener { openNote(position) }
        return view
    }


    private fun openNote(id: Int) {
        val myIntent = Intent(context, OpenNoteActivity::class.java)
        myIntent.putExtra("NoteID", id)
        context.startActivity(myIntent)
    }
}