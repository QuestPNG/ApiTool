package com.beck.apitool.Databases_Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SessionDao {
    @Query ("SELECT *  FROM session")
    fun getAll(): List<Session>

    @Insert
    fun insertAll(vararg sessions: Session)

    @Delete
    fun delete(user: Session)

    @Query("SELECT * FROM session WHERE title = :title")
    fun findByTitle(title: String): Session?

    @Insert
    fun create(title: String, data: String)

    @Update
    fun updateSession(vararg sessions: Session)
}