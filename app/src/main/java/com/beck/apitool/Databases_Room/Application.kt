package com.beck.apitool.Databases_Room

import android.app.Application
import android.content.Context
import androidx.room.Room

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "app-database"
        ).build()


    }
}