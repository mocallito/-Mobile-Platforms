package com.example.report

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/*
data class TemplateState(
    val templates: List<Template> = emptyList(),
    val email: String = "",
    val name: String = "",
    val type: String = "",
    val setting: Boolean = false
)
*/
@Entity
public data class Address (
    val street: String = "",
    val city: String = "" )

@Entity
public class Person (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String = "",
    val addresses: String = ""
)

@Entity(tableName = "Emails")
data class Email(
    var name: String = "",
    var email: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}