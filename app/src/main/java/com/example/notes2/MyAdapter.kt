package com.example.notes2


import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val notes: ArrayList<Note>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.list_item, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemNote = notes[position]
        holder.bindNote(itemNote)
        holder.button.setOnClickListener {
            val context = holder.button.context
            val dropDownMenu = PopupMenu(context, holder.button)
            dropDownMenu.menuInflater.inflate(R.menu.drop_down_menu, dropDownMenu.menu)
            dropDownMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "delete") {
                    MyDB.removeNote(position)
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, notes.size);
                }
                if (menuItem.title == "info") {
                    //todo info menu
                }
                true
            }
            dropDownMenu.show()
        }
    }

    override fun getItemCount() = notes.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var note: Note? = null
        lateinit var titleTextView: TextView
        lateinit var subtitleTextView: TextView
        lateinit var button: ImageButton


        init {
            v.setOnClickListener(this)
        }

        fun bindNote(note: Note) {
            this.note = note
            titleTextView = view.findViewById(R.id.title)
            subtitleTextView = view.findViewById(R.id.text)
            button = view.findViewById(R.id.button)
            titleTextView.text = note.title
            subtitleTextView.text = note.text


        }


        override fun onClick(v: View?) {
            val context = itemView.context
            val myIntent = Intent(context, OpenNoteActivity::class.java)
            myIntent.putExtra(NOTE_KEY, adapterPosition)
            context.startActivity(myIntent)
        }

        companion object {
            private val NOTE_KEY = "NoteID"
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

}


//override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//    val view: View
//    val holder: MyAdapter.ViewHolder
//    if (convertView == null) {
//        val inflater: LayoutInflater = LayoutInflater.from(context)
//        view = inflater.inflate(R.layout.list_item, parent, false)
//        holder = MyAdapter.ViewHolder()
//        holder.titleTextView = view.findViewById<TextView>(R.id.title)
//        holder.subtitleTextView = view.findViewById<TextView>(R.id.text)
//        holder.button = view.findViewById<ImageButton>(R.id.button)
//        view.tag = holder
//    } else {
//        view = convertView
//        holder = convertView.tag as MyAdapter.ViewHolder
//    }
//
//    val title = holder.titleTextView
//    val text = holder.subtitleTextView
//    val button = holder.button
//
//    title.text = notes[position].title
//    text.text = notes[position].text
//
//    //delete btn Event
//    button.setOnClickListener {
//        val dropDownMenu = PopupMenu(context, button)
//        dropDownMenu.menuInflater.inflate(R.menu.drop_down_menu, dropDownMenu.menu)
//        dropDownMenu.setOnMenuItemClickListener { menuItem ->
//            if (menuItem.title == "delete") {
//                MyDB.removeNote(position)
//                notifyDataSetChanged()
//            }
//            if (menuItem.title == "info") {
//                //todo info menu
//            }
//            true
//        }
//        dropDownMenu.show()
//    }
//    view.setOnClickListener { openNote(position) }
//    return view
//}