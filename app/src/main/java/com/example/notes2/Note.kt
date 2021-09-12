package com.example.notes2

import android.app.LauncherActivity

class Note() : LauncherActivity.ListItem() {
    var id: Long = 0
    lateinit var title: String
    lateinit var text: String

    constructor(text: String,  title: String) : this(text) {
        this.title = title
    }

    constructor(text: String) : this() {
        this.title = text.substring(0, 6) + " "
    }
}
