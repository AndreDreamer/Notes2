package com.example.notes2


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MyAdapter(private val context: Activity, private val notes: ArrayList<Note>) :
    ArrayAdapter<Note>(context, R.layout.list_item, notes) {

     val NAME_OF_EXTRA : String = "NoteID"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_item, parent, false)
            holder = ViewHolder()
            holder.titleTextView = view.findViewById<TextView>(R.id.title)
            holder.subtitleTextView = view.findViewById<TextView>(R.id.text)
            holder.button = view.findViewById<Button>(R.id.button)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val title = holder.titleTextView
        val text = holder.subtitleTextView
        val button = holder.button

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
        view.setOnClickListener { openNote(position) }
        return view
    }

    private class ViewHolder {
        lateinit var titleTextView: TextView
        lateinit var subtitleTextView: TextView
        lateinit var button: Button
    }

    private fun openNote(id: Int) {
        val myIntent = Intent(context, OpenNoteActivity::class.java)
        myIntent.putExtra(NAME_OF_EXTRA, id)
        context.startActivity(myIntent)
    }

}