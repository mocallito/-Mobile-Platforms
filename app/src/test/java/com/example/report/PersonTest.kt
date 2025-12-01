package com.example.report

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersonTest {
    private lateinit var person:Person

    @Before
    fun setUp() {
        val addresses = ArrayList<Address>()
        addresses.add(Address("avenue","city"))
        person = Person("Tom", addresses)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getName() {
        assertEquals("Tom", person.name)
    }

    @Test
    fun getAddresses() {
        assertEquals("avenue", person.addresses[0].street)
    }
}