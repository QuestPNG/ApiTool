package com.beck.apitool.Databases_Room

import androidx.room.Entity
import androidx.room.PrimaryKey
//import androidx.room.vo.Entity

@Entity
data class Session (

    @PrimaryKey(autoGenerate = true)
    val isWebsocketSession: Boolean,
    val id: Long = 0,
    val title: String,
    val url: String,
    val headers: String,
    val queryParams: String,
    val body: String?,
    val method: String?
    // val timestamp: Long
)