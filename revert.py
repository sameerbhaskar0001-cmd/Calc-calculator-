import os

screen = open('app/src/main/java/com/example/CalculatorScreen.kt').read()
screen = screen.replace('text = "CONVERSION (${sourceCurrency.code} → ${targetCurrency.code}) [${viewModel.apiStatus.collectAsState().value}]"', 'text = "CONVERSION (${sourceCurrency.code} → ${targetCurrency.code})"')
screen = screen.replace('val df = DecimalFormat("#,##0.####", DecimalFormatSymbols(Locale.US))', 'val df = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))')
screen = screen.replace('.testTag("conversion_banner_card")\n                        .clickable { viewModel.fetchLatestRates() },', '.testTag("conversion_banner_card"),')
with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(screen)

vm = open('app/src/main/java/com/example/CalculatorViewModel.kt').read()
vm = vm.replace('val currencyFormat = DecimalFormat("#.####", DecimalFormatSymbols(Locale.US))', 'val currencyFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))')
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(vm)

api = """package com.example

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    @Json(name = "result") val result: String,
    @Json(name = "base_code") val baseCode: String,
    @Json(name = "time_last_update_utc") val timeLastUpdateUtc: String?,
    @Json(name = "time_next_update_utc") val timeNextUpdateUtc: String?,
    @Json(name = "rates") val rates: Map<String, Double>
)

interface CurrencyApiService {
    @GET("v6/latest/{base}")
    suspend fun getLatestRates(@Path("base") base: String): CurrencyResponse
}

object CurrencyApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://open.er-api.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val service: CurrencyApiService = retrofit.create(CurrencyApiService::class.java)
}
"""
with open('app/src/main/java/com/example/CurrencyApi.kt', 'w') as f:
    f.write(api)
