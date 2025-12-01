package com.example.report

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DraftDao {
    @Query("SELECT * FROM image_table")
    fun readDraft(): LiveData<Draft>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: Draft)

    @Update
    suspend fun updateDraft(draft: Draft)

    @Query("DELETE FROM image_table")
    suspend fun deleteDraft()
}