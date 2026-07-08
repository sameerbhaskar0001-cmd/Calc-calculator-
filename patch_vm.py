import re
content = open('app/src/main/java/com/example/CalculatorViewModel.kt').read()
content = content.replace('android.util.Log.e("CURRENCY_API", "Error fetching rates", e)', 'e.printStackTrace()')

# Restore error state block
old_err_block = """                } else {
                    _apiStatus.value = ApiStatus.ERROR
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _apiStatus.value = ApiStatus.ERROR
                _lastUpdated.value = e.message ?: "Error"
            }"""
new_err_block = """                } else {
                    _apiStatus.value = ApiStatus.ERROR
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _apiStatus.value = ApiStatus.ERROR
            }"""
content = content.replace(old_err_block, new_err_block)
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(content)
