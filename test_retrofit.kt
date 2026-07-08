package com.example

import kotlinx.coroutines.runBlocking
import java.io.File

fun main() {
    runBlocking {
        try {
            val response = CurrencyApi.service.getLatestRates("USD")
            println("SUCCESS: " + response.result)
            println("INR RATE: " + response.rates["INR"])
        } catch (e: Exception) {
            println("ERROR: " + e.message)
            e.printStackTrace()
        }
    }
}
