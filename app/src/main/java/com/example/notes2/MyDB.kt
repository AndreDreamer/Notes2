package com.example.notes2

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import java.util.ArrayList

class MyDB(context: Context) {
    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putNotes(notes: ArrayList<Note>) {
        val titles = emptyArray<String>()
        val texts = emptyArray<String>()

        for (i in 0..notes.size) {
            titles[i] = notes[i].title
            texts[i] = notes[i].text
        }
        preferences.edit().putString("notes1", TextUtils.join("‚‗‚", titles)).apply()
        preferences.edit().putString("notes2", TextUtils.join("‚‗‚", texts)).apply()
    }

    fun getNotes(): ArrayList<Note> {
        val notes = ArrayList<Note>()
        val titles = TextUtils.split(preferences.getString("notes1", ""), "‚‗‚")
        val texts = TextUtils.split(preferences.getString("notes2", ""), "‚‗‚")

        for (i in 0..titles.size) {
            notes.add( Note(titles[i], texts[i]))
        }
        return notes
    }
}
