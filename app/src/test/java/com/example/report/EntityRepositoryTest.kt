package com.example.report

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.jvm.Throws
import org.junit.Before
import org.junit.Test
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EntityRepositoryTest {
    private lateinit var entityRepository: EntityRepository
    private lateinit var db: EntityDatabase

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, EntityDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        entityRepository = EntityRepository(db.entityDao())
    }
    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }
    /*
    @Test
    @Throws(IOException::class)
    fun insertAndGetReadPerson()  = runBlocking {
        val addresses = ArrayList<Address>();
        addresses.add(Address("asd", "asda"))
        val person = Person("Tom", addresses)
        entityRepository.insertPerson(person)
        val allEntity: List<Person> = entityRepository.readPersonNoLive
        assertEquals(allEntity[0].name, person.name)
    }

     */
}