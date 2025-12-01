package com.example.report;

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Email::class],version = 1, exportSchema = false)
abstract class EntityDatabase : RoomDatabase() {
    abstract fun entityDao(): EntityDao
    companion object {
        private var INSTANCE:EntityDatabase?= null
        fun getDatabase(context: Context): EntityDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also{INSTANCE = it}
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, EntityDatabase::class.java,
                "entity_database").build()
    }
}
