package com.example.notes2


import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import java.util.ArrayList

object MyDB {

    private lateinit var preferences: SharedPreferences
    private const val PREFS_NAME = "params"
    private val notes: ArrayList<Note> = arrayListOf()

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val titles = TextUtils.split(preferences.getString("notes1", ""), "‚‗‚")
        val texts = TextUtils.split(preferences.getString("notes2", ""), "‚‗‚")
        for (i in titles.indices) {
            notes.add(Note(titles[i], texts[i]))
        }
    }


    fun addNote(note: Note) {
        notes.add(note)
        putNotesToDB()
    }

    fun removeNote(id: Int) {
        notes.removeAt(id)
        putNotesToDB()
    }

    fun getNote(id: Int): Note {
        return notes[id]
    }

    fun getNotes(): ArrayList<Note> {
        return notes
    }

    fun setNote(id: Int, note: Note) {
        notes[id] = note
        putNotesToDB()
    }

    private fun putNotesToDB() {

        val titles = arrayOfNulls<String>(notes.size)
        val texts = arrayOfNulls<String>(notes.size)

        for (i in notes.indices) {
            titles[i] = notes[i].title
            texts[i] = notes[i].text
        }
        preferences.edit().putString("notes1", TextUtils.join("‚‗‚", titles)).apply()
        preferences.edit().putString("notes2", TextUtils.join("‚‗‚", texts)).apply()
    }
}
