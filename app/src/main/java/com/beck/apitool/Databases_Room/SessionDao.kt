package com.beck.apitool.Databases_Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SessionDao {
    @Query ("SELECT *  FROM session")
    suspend fun getAll(): List<Session>

    @Insert
    suspend fun insertAll(vararg sessions: Session)

    @Delete
    suspend fun delete(user: Session)

    @Query("SELECT * FROM session WHERE title = :title")
    suspend fun findByTitle(title: String): Session?

    @Insert
    suspend fun create(session: Session)

    @Update
    suspend fun updateSession(vararg sessions: Session)
}