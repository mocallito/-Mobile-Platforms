package com.example.report

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "template_table")
data class Template(
    //@PrimaryKey(autoGenerate = true)
    //val id: Int = 0,
    var name: String = "",
    var type: String = "",
    var inuse: Boolean = false,
    var autofillTitle: Boolean = true,
    var userYourlocation: Boolean = true,
    var syncedTime: Boolean = true,
    var selected: Boolean = true
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}