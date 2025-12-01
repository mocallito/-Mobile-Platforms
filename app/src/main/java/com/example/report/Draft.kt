package com.example.report

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Draft(
    val takenPhoto: String,
    var titleFill: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}