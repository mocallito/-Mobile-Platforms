package com.example.report

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.Arrays

class Converters {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }
    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap {
        //if(byteArray == null)
        //    return Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)//a dummy
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
    }
}