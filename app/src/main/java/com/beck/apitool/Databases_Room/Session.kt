package com.beck.apitool.Databases_Room

import androidx.room.Entity
import androidx.room.PrimaryKey
//import androidx.room.vo.Entity

@Entity
data class Session (
    @PrimaryKey
    val title: String,
    val isWebsocketSession: Boolean,
    val url: String,
    val headers: String,
    val queryParams: String,
    val body: String?,
    val method: String?
    // val timestamp: Long
)