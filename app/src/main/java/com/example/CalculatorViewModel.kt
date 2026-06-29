package com.example

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class CurrencyField {
    USD, INR
}

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    val emoji: String,
    val defaultUsdRate: Double
)

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("exchange_calc_prefs", Context.MODE_PRIVATE)

    // --- Supported Currencies list ---
    val currencies = listOf(
        Currency("USD", "US Dollar", "$", "🇺🇸", 1.0),
        Currency("INR", "Indian Rupee", "₹", "🇮🇳", 83.50),
        Currency("EUR", "Euro", "€", "🇪🇺", 0.92),
        Currency("GBP", "British Pound", "£", "🇬🇧", 0.79),
        Currency("JPY", "Japanese Yen", "¥", "🇯🇵", 155.20),
        Currency("CAD", "Canadian Dollar", "$", "🇨🇦", 1.37),
        Currency("AUD", "Australian Dollar", "$", "🇦🇺", 1.51),
        Currency("AED", "UAE Dirham", "د.إ", "🇦🇪", 3.67),
        Currency("CNY", "Chinese Yuan", "¥", "🇨🇳", 7.26)
    )

    private val _sourceCurrency = MutableStateFlow(currencies[0]) // default USD
    val sourceCurrency: StateFlow<Currency> = _sourceCurrency.asStateFlow()

    private val _targetCurrency = MutableStateFlow(currencies[1]) // default INR
    val targetCurrency: StateFlow<Currency> = _targetCurrency.asStateFlow()

    // --- Calculator State ---
    private val _expression = MutableStateFlow("")
    val expression: StateFlow<String> = _expression.asStateFlow()

    private val _calcResult = MutableStateFlow("")
    val calcResult: StateFlow<String> = _calcResult.asStateFlow()

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history.asStateFlow()

    // --- Currency Exchange State ---
    private val _exchangeRate = MutableStateFlow(83.50)
    val exchangeRate: StateFlow<Double> = _exchangeRate.asStateFlow()

    private val _usdInput = MutableStateFlow("")
    val usdInput: StateFlow<String> = _usdInput.asStateFlow()

    private val _inrInput = MutableStateFlow("")
    val inrInput: StateFlow<String> = _inrInput.asStateFlow()

    private val _activeCurrencyField = MutableStateFlow(CurrencyField.USD)
    val activeCurrencyField: StateFlow<CurrencyField> = _activeCurrencyField.asStateFlow()

    // --- Custom Canvas Bezier Trend Chart State ---
    private val _historicalRates = MutableStateFlow<List<Double>>(emptyList())
    val historicalRates: StateFlow<List<Double>> = _historicalRates.asStateFlow()

    private val _historicalDates = MutableStateFlow<List<String>>(emptyList())
    val historicalDates: StateFlow<List<String>> = _historicalDates.asStateFlow()

    // --- Smart Split Bill State ---
    private val _numPeople = MutableStateFlow(2)
    val numPeople: StateFlow<Int> = _numPeople.asStateFlow()

    private val _tipPercentage = MutableStateFlow(15)
    val tipPercentage: StateFlow<Int> = _tipPercentage.asStateFlow()

    // --- Settings / Sound & Haptic State ---
    private val _soundEnabled = MutableStateFlow(prefs.getBoolean("sound_enabled", true))
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _hapticProfile = MutableStateFlow(prefs.getString("haptic_profile", "Crisp") ?: "Crisp")
    val hapticProfile: StateFlow<String> = _hapticProfile.asStateFlow()

    private val decimalFormat = DecimalFormat("#.######", DecimalFormatSymbols(Locale.US))
    private val currencyFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))

    init {
        val srcCode = prefs.getString("source_currency", "USD") ?: "USD"
        val tgtCode = prefs.getString("target_currency", "INR") ?: "INR"
        _sourceCurrency.value = currencies.find { it.code == srcCode } ?: currencies[0]
        _targetCurrency.value = currencies.find { it.code == tgtCode } ?: currencies[1]

        // Load custom rates if they exist in prefs
        _history.value = listOf("100 USD = 8350 INR", "50 USD = 4175 INR")
        updateExchangeRateFlow()
        updateHistoricalRates()
    }

    // --- Settings Methods ---
    fun toggleSound() {
        val next = !_soundEnabled.value
        _soundEnabled.value = next
        prefs.edit().putBoolean("sound_enabled", next).apply()
    }

    fun selectHapticProfile(profile: String) {
        _hapticProfile.value = profile
        prefs.edit().putString("haptic_profile", profile).apply()
    }

    fun triggerKeypressEffects(context: android.content.Context) {
        // play haptic feedback
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? android.os.Vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            val duration = when (_hapticProfile.value) {
                "Crisp" -> 12L
                "Soft" -> 5L
                "Heavy" -> 30L
                else -> 0L
            }
            if (duration > 0) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(android.os.VibrationEffect.createOneShot(duration, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(duration)
                }
            }
        }

        // play native click sound
        if (_soundEnabled.value) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? android.media.AudioManager
            audioManager?.playSoundEffect(android.media.AudioManager.FX_KEY_CLICK)
        }
    }

    // --- Split Bill Methods ---
    fun incrementPeople() {
        _numPeople.value = (_numPeople.value + 1).coerceAtMost(100)
    }

    fun decrementPeople() {
        _numPeople.value = (_numPeople.value - 1).coerceAtLeast(1)
    }

    fun updateTipPercentage(pct: Int) {
        _tipPercentage.value = pct.coerceIn(0, 100)
    }

    // --- Currency Selector Methods ---
    fun selectSourceCurrency(currency: Currency) {
        _sourceCurrency.value = currency
        prefs.edit().putString("source_currency", currency.code).apply()
        updateExchangeRateFlow()
        recalculateConversion(fromSource = _activeCurrencyField.value == CurrencyField.USD, input = if (_activeCurrencyField.value == CurrencyField.USD) _usdInput.value else _inrInput.value)
        updateHistoricalRates()
    }

    fun selectTargetCurrency(currency: Currency) {
        _targetCurrency.value = currency
        prefs.edit().putString("target_currency", currency.code).apply()
        updateExchangeRateFlow()
        recalculateConversion(fromSource = _activeCurrencyField.value == CurrencyField.USD, input = if (_activeCurrencyField.value == CurrencyField.USD) _usdInput.value else _inrInput.value)
        updateHistoricalRates()
    }

    fun swapCurrencies() {
        val temp = _sourceCurrency.value
        _sourceCurrency.value = _targetCurrency.value
        _targetCurrency.value = temp
        prefs.edit()
            .putString("source_currency", _sourceCurrency.value.code)
            .putString("target_currency", _targetCurrency.value.code)
            .apply()

        // Swap actual inputs for seamless conversion swap
        val tempInput = _usdInput.value
        _usdInput.value = _inrInput.value
        _inrInput.value = tempInput

        updateExchangeRateFlow()
        recalculateConversion(fromSource = _activeCurrencyField.value == CurrencyField.USD, input = if (_activeCurrencyField.value == CurrencyField.USD) _usdInput.value else _inrInput.value)
        updateHistoricalRates()
    }

    fun getRate(currency: Currency): Double {
        if (currency.code == "USD") return 1.0
        return prefs.getFloat("rate_${currency.code}", currency.defaultUsdRate.toFloat()).toDouble()
    }

    private fun updateExchangeRateFlow() {
        val src = _sourceCurrency.value
        val tgt = _targetCurrency.value
        val srcRate = getRate(src)
        val tgtRate = getRate(tgt)
        _exchangeRate.value = tgtRate / srcRate
    }

    fun updateHistoricalRates() {
        val src = _sourceCurrency.value
        val tgt = _targetCurrency.value
        val baseRate = getRate(tgt) / getRate(src)

        // Generate 7 days of dates
        val dates = ArrayList<String>()
        val sdf = java.text.SimpleDateFormat("dd MMM", Locale.US)
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, -6)
        for (i in 0 until 7) {
            dates.add(sdf.format(cal.time))
            cal.add(java.util.Calendar.DAY_OF_YEAR, 1)
        }
        _historicalDates.value = dates

        // Generate 7 days of stable fluctuating simulated rate feed
        val seed = (src.code + tgt.code).hashCode().toLong()
        val random = java.util.Random(seed)
        val rates = ArrayList<Double>()
        for (i in 0 until 7) {
            val percent = (random.nextDouble() - 0.5) * 0.03 // +/- 1.5% max fluctuation
            rates.add(baseRate * (1.0 + percent))
        }
        rates[6] = baseRate // today is exactly the live rate
        _historicalRates.value = rates
    }

    // --- Calculator Methods ---
    fun onCalcKeyPress(key: String) {
        val currentExpr = _expression.value

        when (key) {
            "C" -> {
                _expression.value = ""
                _calcResult.value = ""
            }
            "⌫" -> {
                if (currentExpr.isNotEmpty()) {
                    _expression.value = currentExpr.dropLast(1)
                    updateLiveResult()
                }
            }
            "=" -> {
                if (currentExpr.isNotEmpty()) {
                    try {
                        val result = evaluateExpression(currentExpr)
                        val formattedResult = formatDouble(result)
                        
                        // Save calculation in history
                        val historyItem = "$currentExpr = $formattedResult"
                        _history.value = (listOf(historyItem) + _history.value).take(20)
                        
                        _expression.value = formattedResult
                        _calcResult.value = ""
                    } catch (e: Exception) {
                        _calcResult.value = "Error"
                    }
                }
            }
            "+/-" -> {
                try {
                    _expression.value = toggleLastNumberSign(currentExpr)
                    updateLiveResult()
                } catch (e: Exception) {
                    // Ignore if invalid
                }
            }
            "%" -> {
                if (currentExpr.isNotEmpty() && currentExpr.last().isDigit()) {
                    _expression.value = currentExpr + "÷100"
                    updateLiveResult()
                }
            }
            "+", "-", "×", "÷" -> {
                if (currentExpr.isNotEmpty()) {
                    val lastChar = currentExpr.last().toString()
                    if (lastChar == "+" || lastChar == "-" || lastChar == "×" || lastChar == "÷") {
                        _expression.value = currentExpr.dropLast(1) + key
                    } else if (lastChar != ".") {
                        _expression.value = currentExpr + key
                    }
                } else if (key == "-") {
                    _expression.value = "-"
                }
            }
            "." -> {
                if (canAppendDecimal(currentExpr)) {
                    _expression.value = currentExpr + "."
                }
            }
            else -> { // Digits 0-9
                _expression.value = currentExpr + key
                updateLiveResult()
            }
        }
    }

    private fun updateLiveResult() {
        val currentExpr = _expression.value
        if (currentExpr.isEmpty()) {
            _calcResult.value = ""
            return
        }
        
        val lastChar = currentExpr.last()
        if (lastChar == '+' || lastChar == '-' || lastChar == '×' || lastChar == '÷' || lastChar == '.') {
            return
        }

        try {
            val result = evaluateExpression(currentExpr)
            _calcResult.value = formatDouble(result)
        } catch (e: Exception) {
            // Keep previous live result or clear it
        }
    }

    private fun canAppendDecimal(expr: String): Boolean {
        if (expr.isEmpty()) return true
        val lastNumber = expr.split("+", "-", "×", "÷").last()
        return !lastNumber.contains(".")
    }

    private fun toggleLastNumberSign(expr: String): String {
        if (expr.isEmpty()) return "-"
        val parts = expr.split("(?<=[+×÷-])|(?=[+×÷-])".toRegex())
        if (parts.isEmpty()) return expr

        val lastPart = parts.last()
        if (lastPart.isEmpty()) return expr

        return if (lastPart.toDoubleOrNull() != null) {
            val invertedValue = -lastPart.toDouble()
            val prefix = expr.substring(0, expr.length - lastPart.length)
            val formattedInverted = formatDouble(invertedValue)
            if (invertedValue < 0 && prefix.isNotEmpty() && prefix.last() == '-') {
                prefix.dropLast(1) + "+" + formattedInverted.drop(1)
            } else {
                prefix + formattedInverted
            }
        } else {
            expr
        }
    }

    private fun formatDouble(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            decimalFormat.format(value)
        }
    }

    // --- Currency Conversion Methods ---
    fun onCurrencyFieldSelect(field: CurrencyField) {
        _activeCurrencyField.value = field
    }

    fun onCurrencyKeyPress(key: String) {
        val isSourceActive = _activeCurrencyField.value == CurrencyField.USD
        val activeInputFlow = if (isSourceActive) _usdInput else _inrInput
        val currentInput = activeInputFlow.value

        when (key) {
            "C" -> {
                _usdInput.value = ""
                _inrInput.value = ""
            }
            "⌫" -> {
                if (currentInput.isNotEmpty()) {
                    val newValue = currentInput.dropLast(1)
                    activeInputFlow.value = newValue
                    recalculateConversion(fromSource = isSourceActive, input = newValue)
                }
            }
            "." -> {
                if (!currentInput.contains(".") && currentInput.isNotEmpty()) {
                    activeInputFlow.value = currentInput + "."
                } else if (currentInput.isEmpty()) {
                    activeInputFlow.value = "0."
                }
            }
            else -> { // Digits 0-9
                if (currentInput == "0" && key == "0") return
                
                val newValue = if (currentInput == "0") key else currentInput + key
                activeInputFlow.value = newValue
                recalculateConversion(fromSource = isSourceActive, input = newValue)
            }
        }
    }

    fun updateExchangeRate(newRate: Double) {
        val src = _sourceCurrency.value
        val tgt = _targetCurrency.value
        if (newRate > 0) {
            if (src.code == "USD") {
                prefs.edit().putFloat("rate_${tgt.code}", newRate.toFloat()).apply()
            } else if (tgt.code == "USD") {
                val invertedRate = 1.0 / newRate
                prefs.edit().putFloat("rate_${src.code}", invertedRate.toFloat()).apply()
            } else {
                val srcUsdRate = getRate(src)
                val newTgtRate = srcUsdRate * newRate
                prefs.edit().putFloat("rate_${tgt.code}", newTgtRate.toFloat()).apply()
            }
            updateExchangeRateFlow()
            recalculateConversion(fromSource = true, input = _usdInput.value)
            updateHistoricalRates()
        }
    }

    fun applyQuickAdd(amount: Double) {
        val isSourceActive = _activeCurrencyField.value == CurrencyField.USD
        if (isSourceActive) {
            val currentVal = _usdInput.value.toDoubleOrNull() ?: 0.0
            val newVal = currentVal + amount
            val formatted = formatDouble(newVal)
            _usdInput.value = formatted
            recalculateConversion(fromSource = true, input = formatted)
        } else {
            val currentVal = _inrInput.value.toDoubleOrNull() ?: 0.0
            val newVal = currentVal + amount
            val formatted = formatDouble(newVal)
            _inrInput.value = formatted
            recalculateConversion(fromSource = false, input = formatted)
        }
    }

    private fun recalculateConversion(fromSource: Boolean, input: String) {
        val numericValue = input.toDoubleOrNull() ?: 0.0
        val src = _sourceCurrency.value
        val tgt = _targetCurrency.value
        val srcRate = getRate(src)
        val tgtRate = getRate(tgt)

        if (input.isEmpty()) {
            if (fromSource) _inrInput.value = "" else _usdInput.value = ""
            return
        }

        if (fromSource) {
            val valueInUsd = numericValue / srcRate
            val targetValue = valueInUsd * tgtRate
            _inrInput.value = currencyFormat.format(targetValue)
        } else {
            val valueInUsd = numericValue / tgtRate
            val sourceValue = valueInUsd * srcRate
            _usdInput.value = currencyFormat.format(sourceValue)
        }
    }

    // --- Expression Parsing Engine (Non-nested Helper State) ---
    private var parsePos = -1
    private var parseCh = -1
    private var parseCleanExpr = ""

    private fun nextChar() {
        parseCh = if (++parsePos < parseCleanExpr.length) parseCleanExpr[parsePos].code else -1
    }

    private fun eat(charToEat: Int): Boolean {
        while (parseCh == ' '.code) nextChar()
        if (parseCh == charToEat) {
            nextChar()
            return true
        }
        return false
    }

    private fun evaluateExpression(expr: String): Double {
        parseCleanExpr = expr.replace("×", "*").replace("÷", "/")
        parsePos = -1
        parseCh = -1
        nextChar()
        val result = parseExpr()
        if (parsePos < parseCleanExpr.length) throw IllegalArgumentException("Unexpected: " + parseCh.toChar())
        return result
    }

    private fun parseExpr(): Double {
        var x = parseTerm()
        while (true) {
            if (eat('+'.code)) x += parseTerm()
            else if (eat('-'.code)) x -= parseTerm()
            else return x
        }
    }

    private fun parseTerm(): Double {
        var x = parseFactor()
        while (true) {
            if (eat('*'.code)) x *= parseFactor()
            else if (eat('/'.code)) {
                val divisor = parseFactor()
                if (divisor == 0.0) throw ArithmeticException("Division by zero")
                x /= divisor
            } else return x
        }
    }

    private fun parseFactor(): Double {
        if (eat('+'.code)) return parseFactor()
        if (eat('-'.code)) return -parseFactor()

        var x: Double
        val startPos = parsePos
        if (eat('('.code)) {
            x = parseExpr()
            eat(')'.code)
        } else if ((parseCh >= '0'.code && parseCh <= '9'.code) || parseCh == '.'.code) {
            while ((parseCh >= '0'.code && parseCh <= '9'.code) || parseCh == '.'.code) nextChar()
            val substring = parseCleanExpr.substring(startPos, parsePos)
            x = substring.toDoubleOrNull() ?: 0.0
        } else {
            throw IllegalArgumentException("Unexpected character: " + parseCh.toChar())
        }
        return x
    }
}
