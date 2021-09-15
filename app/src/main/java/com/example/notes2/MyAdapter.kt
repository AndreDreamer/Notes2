package com.example.notes2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView

class MyAdapter(var context: Context, var notes: ArrayList<Note>) :

    BaseAdapter() {
    var inflatter: LayoutInflater =
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

    override fun getCount(): Int {
        return notes.size
    }

    override fun getItem(id: Int): Any {
        return notes[id]
    }

    override fun getItemId(id: Int): Long {
        return id.toLong()
    }


    override fun getView(id: Int, view: View?, p2: ViewGroup?): View? {

        val title = view?.findViewById<TextView>(R.id.title)
        val text = view?.findViewById<TextView>(R.id.text)

        var textText = ""
        var spaceCounter = 0
        for (i in notes[id].text.indices) {
            if (notes[id].text[i] == '\n') spaceCounter++
            if (spaceCounter > 3) {
                textText += "..."
                break
            }
            if (i > 40) {
                textText += "..."
                break
            }
            textText += notes[id].text[i]
        }
        if (text != null) {
            text.text = textText
        }

        if (notes[id].title.length < 15) {
            if (title != null) {
                title.text = notes[id].title
            }
        } else {
            if (title != null) {
                title.text = notes[id].title.substring(0, 15) + "..."
            }
        }
        val button = view?.findViewById<ImageButton>(R.id.button)
        //delete btn Event
        button?.setOnClickListener {
            val dropDownMenu = PopupMenu(context, button)
            dropDownMenu.menuInflater.inflate(R.menu.drop_down_menu, dropDownMenu.menu)
            dropDownMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "delete") {
                    notes.removeAt(id)
                    MyDB.removeNote(id)
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
        view?.setOnClickListener { openNote(id) }

        return view
    }

    private fun openNote(id: Int) {
        val myIntent = Intent(context, OpenNoteActivity::class.java)
        myIntent.putExtra("NoteID", id)
        context.startActivity(myIntent)
    }
}