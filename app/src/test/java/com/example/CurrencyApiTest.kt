package com.example

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class CurrencyApiTest {
    @Test
    fun testFetch() = runBlocking {
        try {
            val response = CurrencyApi.service.getLatestRates("USD")
            println("API RESULT: " + response.result)
            println("API RATE INR: " + response.rates["INR"])
            assertEquals("success", response.result)
        } catch (e: Exception) {
            println("ERROR: " + e.message)
            e.printStackTrace()
            fail("Exception thrown: ${e.message}")
        }
    }
}
