content = open('app/src/main/java/com/example/CalculatorViewModel.kt').read()
content = content.replace('val r = prefs.getFloat("rate_${currency.code}", currency.defaultUsdRate.toFloat()).toDouble(); android.util.Log.d("CURRENCY", "getRate ${currency.code} = $r"); return r("rate_${currency.code}", currency.defaultUsdRate.toFloat()).toDouble()', 'return prefs.getFloat("rate_${currency.code}", currency.defaultUsdRate.toFloat()).toDouble()')
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(content)
