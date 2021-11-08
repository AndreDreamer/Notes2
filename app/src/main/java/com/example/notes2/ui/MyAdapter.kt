package com.example.notes2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notes2.MyDB
import com.example.notes2.R
import com.example.notes2.model.Note

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
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, notes.size)
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
            normalizeText(note)
        }

        private fun normalizeText(note: Note) {
            var textText = ""
            var spaceCounter = 0
            for (i in note.text.indices) {
                if (note.text[i] == '\n') spaceCounter++
                if (spaceCounter > COUNT_OF_SPACE_TO_CUT) {
                    textText += "..."
                    break
                }
                if (i > COUNT_OF_SYMBOLS_TO_CUT) {
                    textText += "..."
                    break
                }
                textText += note.text[i]
            }
            subtitleTextView.text = textText

        }

        override fun onClick(v: View?) {
            val action =
                MainScreenFragmentDirections.actionMainScreenFragmentToOpenNoteScreenFragment()
            action.noteid = adapterPosition
            v?.findNavController()?.navigate(action)

        }

        companion object {
            private const val COUNT_OF_SPACE_TO_CUT = 3
            private const val COUNT_OF_SYMBOLS_TO_CUT = 40
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

}

