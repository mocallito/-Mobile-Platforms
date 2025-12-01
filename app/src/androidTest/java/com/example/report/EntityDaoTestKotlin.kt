package com.example.report

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class EntityDaoTestKotlin {
    private lateinit var entityDao: EntityDao
    private lateinit var db: EntityDatabase

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, EntityDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        entityDao = db.entityDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }/*
    @Test
    @Throws(Exception::class)
    fun nsertAndGetEntity() = runBlocking {
        val addresses: List<Address> = ArrayList()
        val person = Person("Tom", addresses)
        entityDao.insertEntity(person)
        val allEntity = entityDao.getAllPerson().getOrAwaitValue()
        //assertEquals(allEntity[0].name, "person.name")
        assertThat(allEntity).contains(person)
    }
*/
}