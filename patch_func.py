import re
content = open('app/src/main/java/com/example/CalculatorViewModel.kt').read()
replacement = """    fun fetchLatestRates() {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                val response = CurrencyApi.service.getLatestRates("USD")
                if (response.result == "success") {
                    val editor = prefs.edit()
                    currencies.forEach { currency ->
                        if (currency.code != "USD") {
                            val rateInResponse = response.rates[currency.code]
                            if (rateInResponse != null && rateInResponse > 0) {
                                editor.putFloat("rate_${currency.code}", rateInResponse.toFloat())
                            }
                        }
                    }
                    val updateTime = response.timeLastUpdateUtc ?: java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(java.util.Date())
                    editor.putString("last_rates_update", updateTime)
                    editor.apply()

                    _lastUpdated.value = updateTime
                    _apiStatus.value = ApiStatus.SUCCESS

                    // Refresh active conversions and flows
                    updateExchangeRateFlow()
                    recalculateConversion(fromSource = _activeCurrencyField.value == CurrencyField.USD, input = if (_activeCurrencyField.value == CurrencyField.USD) _usdInput.value else _inrInput.value)
                    updateHistoricalRates()
                } else {
                    _apiStatus.value = ApiStatus.ERROR
                }
            } catch (e: Exception) {
                android.util.Log.e("CURRENCY_API", "Error fetching rates", e)
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }"""
content = re.sub(r'    fun fetchLatestRates\(\) \{.*?(?=    // --- Settings Methods ---)', replacement + '\n\n', content, flags=re.DOTALL)
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(content)
