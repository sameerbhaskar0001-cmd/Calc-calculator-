content = open('app/src/main/java/com/example/CalculatorViewModel.kt').read()
content = content.replace('                    _apiStatus.value = ApiStatus.ERROR_lastUpdated.value = e.message ?: "Error"}', '                    _apiStatus.value = ApiStatus.ERROR\n                }')
content = content.replace('                _apiStatus.value = ApiStatus.ERROR_lastUpdated.value = e.message ?: "Error"}', '                _apiStatus.value = ApiStatus.ERROR\n                _lastUpdated.value = e.message ?: "Error"\n            }')
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(content)
