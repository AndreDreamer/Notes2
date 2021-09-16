package com.example.notes2


class Note(){
    var id: Long = 0
    lateinit var title: String
    lateinit var text: String
    private val countOfSymbolToCut : Int = 6

    constructor(text: String,  title: String) : this() {
        this.title = title
        this.text = text
    }

    constructor(text: String) : this() {
        this.title = text.substring(0, countOfSymbolToCut) + " "
        this.text = text
    }
}
