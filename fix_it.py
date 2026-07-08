import re
content = open('app/src/main/java/com/example/CalculatorViewModel.kt').read()
content = re.sub(r'\} else \{\s*_apiStatus.value = ApiStatus.ERROR_lastUpdated.value = e.message \?: "Error"\}', r'} else {\n                    _apiStatus.value = ApiStatus.ERROR\n                }', content)
content = re.sub(r'\} catch \(e: Exception\) \{\s*android.util.Log.e\("CURRENCY_API", "Error fetching rates", e\)\s*_apiStatus.value = ApiStatus.ERROR_lastUpdated.value = e.message \?: "Error"\}', r'} catch (e: Exception) {\n                android.util.Log.e("CURRENCY_API", "Error fetching rates", e)\n                _apiStatus.value = ApiStatus.ERROR\n            }', content)
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(content)
