package com.example.notes2

import OpenNoteActivity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView

class MyAdapter( var context: Context, var notes: ArrayList<Note>) :
    BaseAdapter() {
    var inflatter: LayoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

    override fun getCount(): Int {
       return notes.size
    }

    override fun getItem(p0: Int): Any {
        return notes[p0]
    }

    override fun getItemId(p0: Int): Long {
      return p0.toLong()
    }

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val vi: View? = p1
        val title = vi?.findViewById<TextView>(R.id.title)
        val text = vi?.findViewById<TextView>(R.id.text)

        var textText = ""
        var spaceCounter = 0
        for (i in notes[p0].text.indices) {
            if (notes[p0].text[i] == '\n') spaceCounter++
            if (spaceCounter > 3) {
                textText += "..."
                break
            }
            if (i > 40) {
                textText += "..."
                break
            }
            textText += notes[p0].text[i]
        }
        if (text != null) {
            text.text = textText
        }

        if (notes[p0].title.length < 15) {
            if (title != null) {
                title.text = notes[p0].title
            }
        } else {
            if (title != null) {
                title.text = notes[p0].title.substring(0, 15) + "..."
            }
        }
        val button = vi.findViewById<ImageButton>(R.id.button)
        //delete btn Event
        button.setOnClickListener {
            val dropDownMenu = PopupMenu(context, button)
            dropDownMenu.menuInflater.inflate(R.menu.drop_down_menu, dropDownMenu.menu)
            dropDownMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "delete") {
                    notes.removeAt(p0)
                    MainActivity.db.putNotes(notes)
                    MainActivity.notes = notes
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
        vi?.setOnClickListener { openNote(p0) }

        return vi!!
    }

    private fun openNote(id: Int) {
        val myIntent = Intent(context, OpenNoteActivity::class.java)
        myIntent.putExtra("NoteID", id)
        context.startActivity(myIntent)
    }
}