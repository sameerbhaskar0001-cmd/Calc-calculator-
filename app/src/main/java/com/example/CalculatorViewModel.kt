package com.example

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class ApiStatus {
    IDLE, LOADING, SUCCESS, ERROR
}

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

    private val _apiStatus = MutableStateFlow(ApiStatus.IDLE)
    val apiStatus: StateFlow<ApiStatus> = _apiStatus.asStateFlow()

    private val _lastUpdated = MutableStateFlow(prefs.getString("last_rates_update", "Never") ?: "Never")
    val lastUpdated: StateFlow<String> = _lastUpdated.asStateFlow()

    // --- Option 4: Secret Vault State ---
    private val _vaultUnlocked = MutableStateFlow(false)
    val vaultUnlocked: StateFlow<Boolean> = _vaultUnlocked.asStateFlow()

    private val _decoyActive = MutableStateFlow(false)
    val decoyActive: StateFlow<Boolean> = _decoyActive.asStateFlow()

    private val _vaultNotes = MutableStateFlow<List<String>>(emptyList())
    val vaultNotes: StateFlow<List<String>> = _vaultNotes.asStateFlow()

    private val _vaultFiles = MutableStateFlow<List<String>>(emptyList())
    val vaultFiles: StateFlow<List<String>> = _vaultFiles.asStateFlow()

    private val _intruderAttempts = MutableStateFlow<List<String>>(emptyList())
    val intruderAttempts: StateFlow<List<String>> = _intruderAttempts.asStateFlow()

    // --- Advanced Stealth & Vault States ---
    private val _biometricEnabled = MutableStateFlow(prefs.getBoolean("biometric_enabled", false))
    val biometricEnabled: StateFlow<Boolean> = _biometricEnabled.asStateFlow()

    private val _panicEnabled = MutableStateFlow(prefs.getBoolean("panic_enabled", false))
    val panicEnabled: StateFlow<Boolean> = _panicEnabled.asStateFlow()

    private val _panicAction = MutableStateFlow(prefs.getString("panic_action", "lock") ?: "lock")
    val panicAction: StateFlow<String> = _panicAction.asStateFlow()

    private val _activeAppIcon = MutableStateFlow(prefs.getString("active_app_icon", "LauncherCalculator") ?: "LauncherCalculator")
    val activeAppIcon: StateFlow<String> = _activeAppIcon.asStateFlow()

    private val _lockedApps = MutableStateFlow(prefs.getStringSet("locked_apps", emptySet()) ?: emptySet())
    val lockedApps: StateFlow<Set<String>> = _lockedApps.asStateFlow()

    private val _preventScreenshots = MutableStateFlow(prefs.getBoolean("prevent_screenshots", false))
    val preventScreenshots: StateFlow<Boolean> = _preventScreenshots.asStateFlow()

    private val _screenDownLock = MutableStateFlow(prefs.getBoolean("screen_down_lock", false))
    val screenDownLock: StateFlow<Boolean> = _screenDownLock.asStateFlow()

    private val _fileLossProtection = MutableStateFlow(prefs.getBoolean("file_loss_protection", false))
    val fileLossProtection: StateFlow<Boolean> = _fileLossProtection.asStateFlow()

    private val _pendingDeleteSender = MutableStateFlow<android.content.IntentSender?>(null)
    val pendingDeleteSender: StateFlow<android.content.IntentSender?> = _pendingDeleteSender.asStateFlow()

    var pendingDeleteOriginalPath: String = ""

    fun clearPendingDelete() {
        _pendingDeleteSender.value = null
        pendingDeleteOriginalPath = ""
    }

    fun onOriginalFileDeleted(context: android.content.Context) {
        if (pendingDeleteOriginalPath.isNotEmpty()) {
            android.media.MediaScannerConnection.scanFile(
                context, 
                arrayOf(pendingDeleteOriginalPath), 
                null 
            ) { path, _ ->
                android.util.Log.d("Vault", "Scanned $path after deletion")
            }
            pendingDeleteOriginalPath = ""
        }
    }

    // --- Multi-Language Localization State ---
    private val _selectedLanguage = MutableStateFlow(prefs.getString("selected_language", "en") ?: "en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun setSelectedLanguage(langCode: String) {
        prefs.edit().putString("selected_language", langCode).apply()
        _selectedLanguage.value = langCode
    }

    fun t(key: String): String {
        return TranslationProvider.translate(key, _selectedLanguage.value)
    }

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
        fetchLatestRates()

        // Load initial vault data (Real or Decoy based on decoyActive)
        loadVaultData()

        // Load intruder attempts
        val savedIntruders = prefs.getStringSet("intruder_attempts", emptySet()) ?: emptySet()
        _intruderAttempts.value = savedIntruders.toList().sortedByDescending { it }
    }

    fun fetchLatestRates() {
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
                e.printStackTrace()
                _apiStatus.value = ApiStatus.ERROR
            }
        }
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
                if (tryUnlockVault(currentExpr)) {
                    _expression.value = ""
                    _calcResult.value = "Vault Unlocked!"
                } else if (currentExpr.isNotEmpty()) {
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

    // --- Option 4: Secret Vault Methods ---
    fun getVaultPin(): String {
        return prefs.getString("vault_pin", "7777") ?: "7777"
    }

    fun setVaultPin(newPin: String) {
        prefs.edit().putString("vault_pin", newPin).apply()
    }

    fun getDecoyPin(): String {
        return prefs.getString("decoy_pin", "1111") ?: "1111"
    }

    fun setDecoyPin(newPin: String) {
        prefs.edit().putString("decoy_pin", newPin).apply()
    }

    fun loadVaultData() {
        val isDecoy = _decoyActive.value
        val notesKey = if (isDecoy) "decoy_notes" else "vault_notes"
        val filesKey = if (isDecoy) "decoy_files" else "vault_files"

        val savedNotes = prefs.getStringSet(notesKey, emptySet()) ?: emptySet()
        _vaultNotes.value = savedNotes.toList().sortedByDescending { it }

        val savedFiles = prefs.getStringSet(filesKey, emptySet()) ?: emptySet()
        _vaultFiles.value = savedFiles.toList().sortedByDescending { it }
    }

    fun tryUnlockVault(pin: String): Boolean {
        if (pin == getVaultPin()) {
            unlockVault(isDecoy = false)
            return true
        } else if (pin == getDecoyPin()) {
            unlockVault(isDecoy = true)
            return true
        } else {
            if (pin.all { it.isDigit() } && pin.length >= 4) {
                logFailedUnlockAttempt(pin)
            }
        }
        return false
    }

    fun unlockVault(isDecoy: Boolean) {
        _decoyActive.value = isDecoy
        loadVaultData()
        _vaultUnlocked.value = true
    }

    fun lockVault() {
        _vaultUnlocked.value = false
        _decoyActive.value = false
        loadVaultData()
    }

    fun logFailedUnlockAttempt(pin: String) {
        val timestamp = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault()).format(java.util.Date())
        val attemptSerialized = "$timestamp|||$pin"
        val updatedAttempts = listOf(attemptSerialized) + _intruderAttempts.value
        val limitedAttempts = updatedAttempts.take(50) // keep last 50
        _intruderAttempts.value = limitedAttempts
        prefs.edit().putStringSet("intruder_attempts", limitedAttempts.toSet()).apply()
    }

    fun clearIntruderAttempts() {
        _intruderAttempts.value = emptyList()
        prefs.edit().remove("intruder_attempts").apply()
    }

    fun addVaultNote(title: String, content: String) {
        val timestamp = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(java.util.Date())
        val noteSerialized = "$timestamp|||$title|||$content"
        val updatedNotes = _vaultNotes.value + noteSerialized
        _vaultNotes.value = updatedNotes.sortedByDescending { it }
        
        val isDecoy = _decoyActive.value
        val notesKey = if (isDecoy) "decoy_notes" else "vault_notes"
        prefs.edit().putStringSet(notesKey, updatedNotes.toSet()).apply()
    }

    fun deleteVaultNote(noteSerialized: String) {
        val updatedNotes = _vaultNotes.value - noteSerialized
        _vaultNotes.value = updatedNotes
        val isDecoy = _decoyActive.value
        val notesKey = if (isDecoy) "decoy_notes" else "vault_notes"
        prefs.edit().putStringSet(notesKey, updatedNotes.toSet()).apply()
    }

    // --- Option 4: Secret Vault File Management ---
    private fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        val index = if (digitGroups < units.size) digitGroups else units.size - 1
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, index.toDouble())) + " " + units[index]
    }

    fun addVaultFile(context: Context, uri: Uri): Boolean {
        return try {
            val contentResolver = context.contentResolver
            
            // Get original name and size
            var originalName = "unnamed_file"
            var mimeType = "application/octet-stream"
            var size = 0L
            var originalPath = ""
            
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIdx != -1) {
                        val nameVal = it.getString(nameIdx)
                        if (!nameVal.isNullOrEmpty()) {
                            originalName = nameVal
                        }
                    }
                    
                    val sizeIdx = it.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIdx != -1) {
                        size = it.getLong(sizeIdx)
                    }
                    
                    val dataIdx = it.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                    if (dataIdx != -1) {
                        originalPath = it.getString(dataIdx) ?: ""
                    }
                }
            }
            
            mimeType = contentResolver.getType(uri) ?: mimeType
            if (mimeType.isEmpty() || mimeType == "application/octet-stream") {
                val ext = File(originalName).extension.lowercase()
                mimeType = when (ext) {
                    "jpg", "jpeg", "png", "webp", "heic", "heif", "gif", "bmp" -> "image/$ext"
                    "mp4", "mkv", "3gp", "avi", "mov", "webm" -> "video/$ext"
                    "pdf" -> "application/pdf"
                    "txt", "csv", "log" -> "text/plain"
                    "zip" -> "application/zip"
                    else -> "application/octet-stream"
                }
            }
            
            val readableSize = formatFileSize(size)
            val id = System.currentTimeMillis().toString()
            val timestamp = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
            
            // Create folder
            val vaultDir = File(context.filesDir, "vault_files")
            if (!vaultDir.exists()) {
                vaultDir.mkdirs()
            }
            
            // Determine file extension
            val extension = File(originalName).extension.ifEmpty {
                when {
                    mimeType.startsWith("image/") -> "jpg"
                    mimeType.startsWith("video/") -> "mp4"
                    mimeType.contains("pdf") -> "pdf"
                    mimeType.contains("text") -> "txt"
                    else -> "dat"
                }
            }
            
            val destFileName = "$id.$extension"
            val destFile = File(vaultDir, destFileName)
            
            // Copy stream
            contentResolver.openInputStream(uri)?.use { input ->
                java.io.FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            val fileSerialized = "$id|||$timestamp|||$originalName|||$mimeType|||${destFile.absolutePath}|||$readableSize"
            val updatedFiles = _vaultFiles.value + fileSerialized
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()

            // Secure Deletion of Original Media File from System Gallery
            try {
                android.util.Log.d("Vault", "Selected Uri: $uri")
                android.util.Log.d("Vault", "Vault copy success: ${destFile.absolutePath}")
                
                // Try to find the actual MediaStore URI if the provided URI is from a picker
                var mediaStoreUri = uri
                var originalPath = ""
                
                val collection = if (mimeType.startsWith("video/")) {
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if (mimeType.startsWith("image/")) {
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else {
                    android.provider.MediaStore.Files.getContentUri("external")
                }
                
                // Find original URI by Display Name and Size
                val projection = arrayOf(android.provider.MediaStore.MediaColumns._ID, android.provider.MediaStore.MediaColumns.DATA)
                val selection = "${android.provider.MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${android.provider.MediaStore.MediaColumns.SIZE} = ?"
                val selectionArgs = arrayOf(originalName, size.toString())
                
                try {
                    contentResolver.query(collection, projection, selection, selectionArgs, null)?.use { mediaCursor ->
                        if (mediaCursor.moveToFirst()) {
                            val idColumn = mediaCursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns._ID)
                            val foundMediaId = mediaCursor.getLong(idColumn)
                            mediaStoreUri = android.content.ContentUris.withAppendedId(collection, foundMediaId)
                            
                            val dataIdx = mediaCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                            if (dataIdx != -1) {
                                originalPath = mediaCursor.getString(dataIdx) ?: ""
                            }
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Failed to query original MediaStore Uri", e)
                }

                android.util.Log.d("Vault", "Original MediaStore Uri: $mediaStoreUri")

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    try {
                        android.util.Log.d("Vault", "Delete request created (API 30+)")
                        val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(mediaStoreUri))
                        pendingDeleteOriginalPath = originalPath
                        _pendingDeleteSender.value = pendingIntent.intentSender
                        android.util.Log.d("Vault", "IntentSender launched")
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to create delete request", e)
                    }
                } else {
                    try {
                        val deletedRows = contentResolver.delete(mediaStoreUri, null, null)
                        if (deletedRows > 0) {
                            android.util.Log.d("Vault", "Delete success (API < 30)")
                            pendingDeleteOriginalPath = originalPath
                            onOriginalFileDeleted(context)
                        } else {
                            android.util.Log.d("Vault", "Delete failure (API < 30), 0 rows deleted")
                        }
                    } catch (securityException: SecurityException) {
                        android.util.Log.e("Vault", "SecurityException during delete (API < 30)", securityException)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && securityException is android.app.RecoverableSecurityException) {
                            pendingDeleteOriginalPath = originalPath
                            _pendingDeleteSender.value = securityException.userAction.actionIntent.intentSender
                            android.util.Log.d("Vault", "RecoverableSecurityException IntentSender launched")
                        }
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("Vault", "Exception in deletion flow", e)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteVaultFile(fileSerialized: String): Boolean {
        return try {
            val parts = fileSerialized.split("|||")
            if (parts.size >= 5) {
                val absolutePath = parts[4]
                val file = File(absolutePath)
                if (file.exists()) {
                    file.delete()
                }
            }
            val updatedFiles = _vaultFiles.value - fileSerialized
            _vaultFiles.value = updatedFiles
            
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun exportVaultFile(
        context: Context,
        fileSerialized: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val parts = fileSerialized.split("|||")
            if (parts.size < 5) {
                onFailure("Invalid file information")
                return
            }
            val originalName = parts[2]
            val mimeType = parts[3]
            val absolutePath = parts[4]
            val file = File(absolutePath)
            if (!file.exists()) {
                onFailure("Source file does not exist")
                return
            }

            val resolver = context.contentResolver
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                val contentValues = android.content.ContentValues().apply {
                    put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, originalName)
                    put(android.provider.MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { output ->
                        file.inputStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                    onSuccess("File exported successfully to Downloads folder!")
                } else {
                    onFailure("Failed to create export file entry")
                }
            } else {
                val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                var destFile = File(downloadsDir, originalName)
                if (destFile.exists()) {
                    val baseName = File(originalName).nameWithoutExtension
                    val ext = File(originalName).extension
                    destFile = File(downloadsDir, "${baseName}_${System.currentTimeMillis()}.$ext")
                }
                file.inputStream().use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
                onSuccess("File exported to Downloads: ${destFile.name}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure("Error: ${e.localizedMessage ?: e.toString()}")
        }
    }

    // --- Advanced Stealth & Settings Updaters ---
    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("biometric_enabled", enabled).apply()
        _biometricEnabled.value = enabled
    }

    fun setPanicEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("panic_enabled", enabled).apply()
        _panicEnabled.value = enabled
    }

    fun setPanicAction(action: String) {
        prefs.edit().putString("panic_action", action).apply()
        _panicAction.value = action
    }



    fun toggleAppLock(appPackageName: String) {
        val current = _lockedApps.value.toMutableSet()
        if (current.contains(appPackageName)) {
            current.remove(appPackageName)
        } else {
            current.add(appPackageName)
        }
        prefs.edit().putStringSet("locked_apps", current).apply()
        _lockedApps.value = current
    }

    fun setPreventScreenshots(enabled: Boolean) {
        prefs.edit().putBoolean("prevent_screenshots", enabled).apply()
        _preventScreenshots.value = enabled
    }

    fun setScreenDownLock(enabled: Boolean) {
        prefs.edit().putBoolean("screen_down_lock", enabled).apply()
        _screenDownLock.value = enabled
    }

    fun setFileLossProtection(enabled: Boolean) {
        prefs.edit().putBoolean("file_loss_protection", enabled).apply()
        _fileLossProtection.value = enabled
    }
}
