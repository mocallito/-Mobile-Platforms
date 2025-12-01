package com.example.report

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TemplateDao {
    @Insert
    suspend fun insert(template: Template): Long

    @Update
    suspend fun update(template: Template)

    @Delete
    suspend fun delete(template: Template)



    //look for those that are selected
    @Query("SELECT name FROM template_table WHERE selected = 1")
    suspend fun getSelected(): List<String>

    //set the item selected but not current to false
    @Query("UPDATE template_table SET selected = 0 WHERE name in (:name)")
    suspend fun setSelected(name: List<String>)

    //look for those that are default
    @Query("SELECT name FROM template_table WHERE inuse = 1")
    suspend fun getDefault(): List<String>

    //set the item default but not current to default
    @Query("UPDATE template_table SET inuse = 0 WHERE name in (:name)")
    suspend fun setDefault(name: List<String>)

    @Query("SELECT * FROM template_table WHERE selected = 1")
    suspend fun getCurrent(): Template

    @Query("UPDATE template_table SET selected = 1 WHERE name = :name")
    suspend fun selectCurrent(name: String)

    @Query("UPDATE template_table SET inuse = 1 WHERE name = :name")
    suspend fun selectDefault(name: String)

    @Query("SELECT * FROM template_table WHERE inuse = 1")
    suspend fun usingTemplate(): Template

    @Query("SELECT * FROM template_table WHERE id = (SELECT max(id) FROM template_table)")
    suspend fun getLatest(): Template

    @Query("DELETE FROM template_table")
    suspend fun deleteAllTemplate()

    @Query("SELECT * FROM template_table")
    fun allTemplate(): LiveData<List<Template>>

    /*
    @Query("SELECT * FROM template_table, Emails WHERE template_table.name = Emails.name")
    LiveData<List<Template>> getAllTemplate();
     */
}