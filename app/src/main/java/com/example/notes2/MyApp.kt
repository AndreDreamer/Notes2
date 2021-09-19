package com.example.notes2

import android.app.Application

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MyDB.init(this)

    }
}