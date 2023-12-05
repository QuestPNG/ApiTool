package com.beck.apitool.Databases_Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session (

    @PrimaryKey(autoGenerate = true)

    val title: String,

    val url:String


)