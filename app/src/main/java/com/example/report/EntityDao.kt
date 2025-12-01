package com.example.report

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Update

/*
sealed interface TemplateEvent {
    object SaveTemplate: TemplateEvent
    data class SetEmail(val email:String): TemplateEvent
    data class SetName(val name:String): TemplateEvent
    data class SetType(val type:String): TemplateEvent
    data class SetSetting(val setting:Boolean): TemplateEvent
    object ShowDialog: TemplateEvent
    object HideDialog: TemplateEvent
    data class DeleteTemplate(val template: Template): TemplateEvent
}*/
@Dao
public interface EntityDao {
    /*
    @Insert
    suspend fun insertEntity(person:Person);

    @Query("SELECT * FROM Person")
    fun getAllPerson(): LiveData<List<Person>>;

    @Query("SELECT * FROM Person")
    fun getAllPersonNoLive(): List<Person>;

     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(email: Email);

    @Query("DELETE FROM Emails")
    suspend fun deleteAllEmail()

    @Delete
    suspend fun delete(email: Email)

    @Update
    suspend fun update(email: Email)

    @Query("DELETE FROM Emails WHERE name = :template")
    suspend fun onTemplateDelete(template: String)

    @Query("SELECT * FROM Emails WHERE name = :template")
    //@Query("SELECT * FROM template_table JOIN Emails ON Emails.name = template_table.name WHERE template_table.inuse = 1")
    fun getAllEmail(template: String): LiveData<List<Email>>;
}