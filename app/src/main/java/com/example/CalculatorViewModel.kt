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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import com.example.ui.theme.AppTheme
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

    private val _isEvaluated = MutableStateFlow(false)
    val isEvaluated: StateFlow<Boolean> = _isEvaluated.asStateFlow()

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

    // --- Advanced Vault Folder, Favorite and Recent States ---
    private val _vaultFolders = MutableStateFlow<List<String>>(emptyList())
    val vaultFolders: StateFlow<List<String>> = _vaultFolders.asStateFlow()

    private val _fileFolders = MutableStateFlow<Map<String, String>>(emptyMap())
    val fileFolders: StateFlow<Map<String, String>> = _fileFolders.asStateFlow()

    private val _noteFolders = MutableStateFlow<Map<String, String>>(emptyMap())
    val noteFolders: StateFlow<Map<String, String>> = _noteFolders.asStateFlow()

    private val _favoriteFiles = MutableStateFlow<Set<String>>(emptySet())
    val favoriteFiles: StateFlow<Set<String>> = _favoriteFiles.asStateFlow()

    private val _favoriteNotes = MutableStateFlow<Set<String>>(emptySet())
    val favoriteNotes: StateFlow<Set<String>> = _favoriteNotes.asStateFlow()

    private val _recentlyOpened = MutableStateFlow<List<RecentItem>>(emptyList())
    val recentlyOpened: StateFlow<List<RecentItem>> = _recentlyOpened.asStateFlow()

    fun loadFolders() {
        val saved = prefs.getStringSet("vault_folders", setOf("Personal", "Work", "Finance")) ?: setOf("Personal", "Work", "Finance")
        _vaultFolders.value = saved.toList().sorted()
    }

    fun addFolder(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        val current = _vaultFolders.value.toSet() + trimmed
        _vaultFolders.value = current.toList().sorted()
        prefs.edit().putStringSet("vault_folders", current).apply()
    }

    fun deleteFolder(name: String) {
        val current = _vaultFolders.value.toSet() - name
        _vaultFolders.value = current.toList().sorted()
        prefs.edit().putStringSet("vault_folders", current).apply()

        // Clean up associations
        val currentFiles = _fileFolders.value.toMutableMap()
        val toRemoveFiles = currentFiles.filter { it.value == name }.keys
        toRemoveFiles.forEach { id ->
            currentFiles.remove(id)
            prefs.edit().remove("folder_file_$id").apply()
        }
        _fileFolders.value = currentFiles

        val currentNotes = _noteFolders.value.toMutableMap()
        val toRemoveNotes = currentNotes.filter { it.value == name }.keys
        toRemoveNotes.forEach { noteStr ->
            currentNotes.remove(noteStr)
            prefs.edit().remove("folder_note_$noteStr").apply()
        }
        _noteFolders.value = currentNotes
    }

    fun setFolderForFile(id: String, folder: String) {
        val current = _fileFolders.value.toMutableMap()
        if (folder.isEmpty() || folder == "Default") {
            current.remove(id)
            prefs.edit().remove("folder_file_$id").apply()
        } else {
            current[id] = folder
            prefs.edit().putString("folder_file_$id", folder).apply()
        }
        _fileFolders.value = current
    }

    fun setFolderForNote(noteStr: String, folder: String) {
        val current = _noteFolders.value.toMutableMap()
        if (folder.isEmpty() || folder == "Default") {
            current.remove(noteStr)
            prefs.edit().remove("folder_note_$noteStr").apply()
        } else {
            current[noteStr] = folder
            prefs.edit().putString("folder_note_$noteStr", folder).apply()
        }
        _noteFolders.value = current
    }

    fun loadFolderAssociations() {
        val fileMap = mutableMapOf<String, String>()
        _vaultFiles.value.forEach { fileStr ->
            val parts = fileStr.split("|||")
            if (parts.isNotEmpty()) {
                val id = parts[0]
                val folder = prefs.getString("folder_file_$id", "") ?: ""
                if (folder.isNotEmpty()) {
                    fileMap[id] = folder
                }
            }
        }
        _fileFolders.value = fileMap

        val noteMap = mutableMapOf<String, String>()
        _vaultNotes.value.forEach { noteStr ->
            val folder = prefs.getString("folder_note_$noteStr", "") ?: ""
            if (folder.isNotEmpty()) {
                noteMap[noteStr] = folder
            }
        }
        _noteFolders.value = noteMap
    }

    fun loadFavorites() {
        _favoriteFiles.value = prefs.getStringSet("favorite_files", emptySet()) ?: emptySet()
        _favoriteNotes.value = prefs.getStringSet("favorite_notes", emptySet()) ?: emptySet()
    }

    fun toggleFavoriteFile(id: String) {
        val current = _favoriteFiles.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _favoriteFiles.value = current
        prefs.edit().putStringSet("favorite_files", current).apply()
    }

    fun toggleFavoriteNote(noteStr: String) {
        val current = _favoriteNotes.value.toMutableSet()
        if (current.contains(noteStr)) {
            current.remove(noteStr)
        } else {
            current.add(noteStr)
        }
        _favoriteNotes.value = current
        prefs.edit().putStringSet("favorite_notes", current).apply()
    }

    fun loadRecentlyOpened() {
        val saved = prefs.getStringSet("recently_opened_items", emptySet()) ?: emptySet()
        val list = saved.mapNotNull { itemStr ->
            val parts = itemStr.split("|||")
            if (parts.size >= 5) {
                RecentItem(
                    type = parts[0],
                    id = parts[1],
                    name = parts[2],
                    extra = parts[3],
                    timestamp = parts[4].toLongOrNull() ?: 0L
                )
            } else null
        }.sortedByDescending { it.timestamp }
        _recentlyOpened.value = list
    }

    fun recordOpenedItem(id: String, type: String, name: String, extra: String = "") {
        val timestamp = System.currentTimeMillis()
        val newItem = RecentItem(id, type, name, timestamp, extra)
        val currentList = _recentlyOpened.value.filterNot { it.id == id && it.type == type }
        val updatedList = (listOf(newItem) + currentList).take(10)
        _recentlyOpened.value = updatedList

        val serialized = updatedList.map { "${it.type}|||${it.id}|||${it.name}|||${it.extra}|||${it.timestamp}" }.toSet()
        prefs.edit().putStringSet("recently_opened_items", serialized).apply()
    }

    fun getVaultStorageDetails(): StorageDetails {
        var photosSize = 0L
        var photosCount = 0
        var videosSize = 0L
        var videosCount = 0
        var documentsSize = 0L
        var documentsCount = 0

        _vaultFiles.value.forEach { fileStr ->
            val parts = fileStr.split("|||")
            if (parts.size >= 5) {
                val mimeType = parts[3]
                val path = parts[4]
                val file = java.io.File(path)
                val size = if (file.exists()) file.length() else 0L

                if (mimeType.startsWith("image/")) {
                    photosSize += size
                    photosCount++
                } else if (mimeType.startsWith("video/")) {
                    videosSize += size
                    videosCount++
                } else {
                    documentsSize += size
                    documentsCount++
                }
            }
        }

        var notesSize = 0L
        val notesCount = _vaultNotes.value.size
        _vaultNotes.value.forEach { noteStr ->
            notesSize += noteStr.toByteArray().size
        }

        val totalSize = photosSize + videosSize + documentsSize + notesSize

        return StorageDetails(
            photosSize = photosSize,
            photosCount = photosCount,
            videosSize = videosSize,
            videosCount = videosCount,
            documentsSize = documentsSize,
            documentsCount = documentsCount,
            notesSize = notesSize,
            notesCount = notesCount,
            totalSize = totalSize
        )
    }

    private val _recentlyDeletedFiles = MutableStateFlow<List<String>>(emptyList())
    val recentlyDeletedFiles: StateFlow<List<String>> = _recentlyDeletedFiles.asStateFlow()

    private val _intruderAttempts = MutableStateFlow<List<String>>(emptyList())
    val intruderAttempts: StateFlow<List<String>> = _intruderAttempts.asStateFlow()

    private val _intruderDetectionEnabled = MutableStateFlow(prefs.getBoolean("intruder_detection_enabled", true))
    val intruderDetectionEnabled: StateFlow<Boolean> = _intruderDetectionEnabled.asStateFlow()

    private val _autoLockDuration = MutableStateFlow(prefs.getInt("auto_lock_duration", -1))
    val autoLockDuration: StateFlow<Int> = _autoLockDuration.asStateFlow()

    private val _blurThumbnails = MutableStateFlow(prefs.getBoolean("blur_thumbnails", false))
    val blurThumbnails: StateFlow<Boolean> = _blurThumbnails.asStateFlow()

    private val _lockedFolders = MutableStateFlow(prefs.getStringSet("locked_folders", emptySet()) ?: emptySet())
    val lockedFolders: StateFlow<Set<String>> = _lockedFolders.asStateFlow()

    private val _tempUnlockedFolders = MutableStateFlow<Set<String>>(emptySet())
    val tempUnlockedFolders: StateFlow<Set<String>> = _tempUnlockedFolders.asStateFlow()

    private val _lastInteractionTime = MutableStateFlow(System.currentTimeMillis())
    val lastInteractionTime: StateFlow<Long> = _lastInteractionTime.asStateFlow()

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

    var pendingDeleteOriginalPaths: List<String> = emptyList()
    var stagedVaultFiles = mutableListOf<String>()

    fun clearPendingDelete() {
        _pendingDeleteSender.value = null
        pendingDeleteOriginalPaths = emptyList()
        // If user denied deletion, we might still want to show the file in the vault, or rollback.
        // Let's just show it.
        if (stagedVaultFiles.isNotEmpty()) {
            val updatedFiles = _vaultFiles.value + stagedVaultFiles
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, _vaultFiles.value.toSet()).apply()
            stagedVaultFiles.clear()
        }
    }

    fun onOriginalFileDeleted(context: android.content.Context) {
        // MediaStore is already updated via createDeleteRequest or contentResolver.delete
        pendingDeleteOriginalPaths = emptyList()
        
        // 7. Refresh the Vault UI.
        if (stagedVaultFiles.isNotEmpty()) {
            val updatedFiles = _vaultFiles.value + stagedVaultFiles
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, _vaultFiles.value.toSet()).apply()
            stagedVaultFiles.clear()
        }
    }

    // --- Multi-Language Localization State ---
    private val _selectedLanguage = MutableStateFlow(prefs.getString("selected_language", "en") ?: "en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun setSelectedLanguage(langCode: String) {
        prefs.edit().putString("selected_language", langCode).apply()
        _selectedLanguage.value = langCode
    }

    // --- Dynamic Theme Selection State ---
    private val _selectedTheme = MutableStateFlow(
        try {
            AppTheme.valueOf(prefs.getString("selected_theme", AppTheme.GRAPHITE.name) ?: AppTheme.GRAPHITE.name)
        } catch (e: Exception) {
            AppTheme.GRAPHITE
        }
    )
    val selectedTheme: StateFlow<AppTheme> = _selectedTheme.asStateFlow()

    fun setSelectedTheme(theme: AppTheme) {
        prefs.edit().putString("selected_theme", theme.name).apply()
        _selectedTheme.value = theme
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

        // Auto Lock loop
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(2000)
                if (_vaultUnlocked.value && _autoLockDuration.value != -1) {
                    val idleMillis = System.currentTimeMillis() - _lastInteractionTime.value
                    val limitMillis = _autoLockDuration.value * 1000L
                    if (idleMillis >= limitMillis) {
                        lockVault()
                    }
                }
            }
        }
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
        val isCurrentEval = _isEvaluated.value

        when (key) {
            "C" -> {
                _expression.value = ""
                _calcResult.value = ""
                _isEvaluated.value = false
            }
            "⌫" -> {
                if (isCurrentEval) {
                    _expression.value = ""
                    _calcResult.value = ""
                    _isEvaluated.value = false
                } else if (currentExpr.isNotEmpty()) {
                    _expression.value = currentExpr.dropLast(1)
                    updateLiveResult()
                }
            }
            "=" -> {
                if (tryUnlockVault(currentExpr)) {
                    _expression.value = ""
                    _calcResult.value = ""
                    _isEvaluated.value = false
                } else if (currentExpr.isNotEmpty() && !isCurrentEval) {
                    try {
                        val result = evaluateExpression(currentExpr)
                        val formattedResult = formatDouble(result)
                        
                        // Save calculation in history
                        val historyItem = "$currentExpr = $formattedResult"
                        _history.value = (listOf(historyItem) + _history.value).take(20)
                        
                        _expression.value = currentExpr
                        _calcResult.value = formattedResult
                        _isEvaluated.value = true
                    } catch (e: Exception) {
                        _calcResult.value = "Error"
                        _isEvaluated.value = true
                    }
                }
            }
            "+/-" -> {
                if (isCurrentEval) {
                    val lastRes = _calcResult.value
                    if (lastRes.isNotEmpty() && lastRes != "Error") {
                        try {
                            _expression.value = toggleLastNumberSign(lastRes)
                            _calcResult.value = ""
                            _isEvaluated.value = false
                            updateLiveResult()
                        } catch (e: Exception) {}
                    }
                } else {
                    try {
                        _expression.value = toggleLastNumberSign(currentExpr)
                        updateLiveResult()
                    } catch (e: Exception) {
                        // Ignore if invalid
                    }
                }
            }
            "%" -> {
                if (isCurrentEval) {
                    val lastRes = _calcResult.value
                    if (lastRes.isNotEmpty() && lastRes != "Error" && lastRes.last().isDigit()) {
                        _expression.value = lastRes + "÷100"
                        _calcResult.value = ""
                        _isEvaluated.value = false
                        updateLiveResult()
                    }
                } else if (currentExpr.isNotEmpty() && currentExpr.last().isDigit()) {
                    _expression.value = currentExpr + "÷100"
                    updateLiveResult()
                }
            }
            "+", "-", "×", "÷" -> {
                if (isCurrentEval) {
                    val lastRes = _calcResult.value
                    if (lastRes.isNotEmpty() && lastRes != "Error") {
                        _expression.value = lastRes + key
                        _calcResult.value = ""
                        _isEvaluated.value = false
                    }
                } else {
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
            }
            "." -> {
                if (isCurrentEval) {
                    _expression.value = "0."
                    _calcResult.value = ""
                    _isEvaluated.value = false
                } else {
                    if (canAppendDecimal(currentExpr)) {
                        _expression.value = currentExpr + "."
                    }
                }
            }
            else -> { // Digits 0-9
                if (isCurrentEval) {
                    _expression.value = key
                    _calcResult.value = ""
                    _isEvaluated.value = false
                    updateLiveResult()
                } else {
                    _expression.value = currentExpr + key
                    updateLiveResult()
                }
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
                if (currentInput == "0" && (key == "0" || key == "00")) return
                
                val newValue = if (currentInput == "0" && key != "00") key else if (currentInput == "0" && key == "00") "0" else currentInput + key
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
        viewModelScope.launch(Dispatchers.IO) {
            val isDecoy = _decoyActive.value
            val notesKey = if (isDecoy) "decoy_notes" else "vault_notes"
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"

            val savedNotes = prefs.getStringSet(notesKey, emptySet()) ?: emptySet()
            _vaultNotes.value = savedNotes.toList().sortedByDescending { it }

            val savedFiles = prefs.getStringSet(filesKey, emptySet()) ?: emptySet()
            _vaultFiles.value = savedFiles.toList().sortedByDescending { it }

            // --- Load & Auto-cleanup Recently Deleted Files ---
            val recentKey = if (isDecoy) "recently_deleted_decoy_files" else "recently_deleted_files"
            val savedRecent = prefs.getStringSet(recentKey, emptySet()) ?: emptySet()
            
            val currentMillis = System.currentTimeMillis()
            val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000
            val validRecent = mutableListOf<String>()
            val toDeleteFiles = mutableListOf<String>()

            for (item in savedRecent) {
                val parts = item.split("|||")
                if (parts.size >= 7) {
                    val deletedTime = parts[6].toLongOrNull() ?: 0L
                    if (currentMillis - deletedTime >= thirtyDaysMillis) {
                        toDeleteFiles.add(item)
                    } else {
                        validRecent.add(item)
                    }
                } else {
                    // Default fallback
                    validRecent.add(item)
                }
            }

            // Permanently delete expired files from disk
            for (item in toDeleteFiles) {
                try {
                    val parts = item.split("|||")
                    if (parts.size >= 5) {
                        val file = File(parts[4])
                        if (file.exists()) {
                            file.delete()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (toDeleteFiles.isNotEmpty()) {
                prefs.edit().putStringSet(recentKey, validRecent.toSet()).apply()
            }

            _recentlyDeletedFiles.value = validRecent.sortedByDescending {
                val parts = it.split("|||")
                if (parts.size >= 7) parts[6].toLongOrNull() ?: 0L else 0L
            }

            loadFolders()
            loadFolderAssociations()
            loadFavorites()
            loadRecentlyOpened()
        }
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
                if (_intruderDetectionEnabled.value) {
                    logFailedUnlockAttempt(pin)
                }
            }
        }
        return false
    }

    fun verifyFolderPin(pin: String): Boolean {
        val expectedPin = if (_decoyActive.value) getDecoyPin() else getVaultPin()
        return pin == expectedPin
    }

    fun unlockVault(isDecoy: Boolean) {
        _decoyActive.value = isDecoy
        loadVaultData()
        updateLastInteraction()
        _vaultUnlocked.value = true
    }

    fun lockVault() {
        _vaultUnlocked.value = false
        _decoyActive.value = false
        _expression.value = ""
        _calcResult.value = ""
        _isEvaluated.value = false
        clearTempUnlockedFolders()
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
    fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        val index = if (digitGroups < units.size) digitGroups else units.size - 1
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, index.toDouble())) + " " + units[index]
    }

    
    fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
        android.util.Log.d("Vault", "batchDeleteOriginalFiles called with ${uris.size} uris")
        if (uris.isEmpty()) return
        try {
            val contentResolver = context.contentResolver
            val mediaStoreUris = mutableListOf<Uri>()
            val urisToPersist = mutableListOf<String>()
            
            for (uri in uris) {
                var resolvedUri = uri
                var path = ""
                try {
                    contentResolver.query(uri, null, null, null, null)?.use {
                        if (it.moveToFirst()) {
                            val dataIdx = it.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                            if (dataIdx != -1) path = it.getString(dataIdx) ?: ""
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Failed to query path for uri: $uri", e)
                }

                if (android.provider.DocumentsContract.isDocumentUri(context, uri) && uri.authority == "com.android.providers.media.documents") {
                    try {
                        val docId = android.provider.DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":")
                        if (split.size >= 2) {
                            val type = split[0]
                            val id = split[1].toLongOrNull()
                            if (id != null) {
                                val baseUri = when (type) {
                                    "image" -> android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    "video" -> android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                    "audio" -> android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                    else -> android.provider.MediaStore.Files.getContentUri("external")
                                }
                                resolvedUri = android.content.ContentUris.withAppendedId(baseUri, id)
                            }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to resolve SAF URI in batch", e)
                    }
                } else if (uri.scheme == "content" && uri.authority?.startsWith("media") == false && path.isNotEmpty()) {
                    try {
                        val mediaCursor = contentResolver.query(
                            android.provider.MediaStore.Files.getContentUri("external"),
                            arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                            "${android.provider.MediaStore.Files.FileColumns.DATA} = ?",
                            arrayOf(path),
                            null
                        )
                        mediaCursor?.use {
                            if (it.moveToFirst()) {
                                val id = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                                resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), id)
                            }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to resolve MediaStore URI for path: $path", e)
                    }
                }
                
                // ONLY add to mediaStoreUris if it's a valid media store URI
                if (resolvedUri.authority?.startsWith("media") == true) {
                    mediaStoreUris.add(resolvedUri)
                } else {
                    android.util.Log.w("Vault", "Cannot delete non-media URI: $resolvedUri")
                }
                urisToPersist.add(resolvedUri.toString())
            }
            
            pendingDeleteOriginalPaths = urisToPersist
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                if (mediaStoreUris.isNotEmpty()) {
                    try {
                        val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                        _pendingDeleteSender.value = pendingIntent.intentSender
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "createDeleteRequest failed", e)
                        // If delete fails, still commit the vault files!
                        onOriginalFileDeleted(context)
                    }
                } else {
                    // Nothing to delete, just commit
                    onOriginalFileDeleted(context)
                }
            } else {
                for (uri in mediaStoreUris) {
                    try {
                        contentResolver.delete(uri, null, null)
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to delete uri: $uri", e)
                    }
                }
                onOriginalFileDeleted(context)
            }
        } catch (e: Exception) {
            android.util.Log.e("Vault", "Batch delete exception", e)
            // Ensure we commit the files even if delete crashes
            onOriginalFileDeleted(context)
        }
    }
    fun addVaultFile(context: Context, uri: Uri, skipDelete: Boolean = false): Boolean {
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
            
            // Selected Uri Log
            android.util.Log.d("Vault", "Selected Uri: $uri")

            // Copy stream
            contentResolver.openInputStream(uri)?.use { input ->
                java.io.FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Verify copy completed successfully
            val isCopySuccessful = destFile.exists() && destFile.length() > 0
            if (!isCopySuccessful) {
                android.util.Log.e("Vault", "Vault copy failed or empty file")
                return false
            }
            android.util.Log.d("Vault", "Vault copy success: ${destFile.absolutePath}")
            
            var durationMs = 0L
            if (mimeType.startsWith("video/")) {
                try {
                    val retriever = android.media.MediaMetadataRetriever()
                    retriever.setDataSource(destFile.absolutePath)
                    val timeStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
                    durationMs = timeStr?.toLongOrNull() ?: 0L
                    retriever.release()
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Failed to extract duration", e)
                }
            }
            
            val fileSerialized = "$id|||$timestamp|||$originalName|||$mimeType|||${destFile.absolutePath}|||$readableSize|||$durationMs"
            stagedVaultFiles.add(fileSerialized)
            if (!skipDelete && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
                // If direct delete (API < 30) was used and skipped sender, the onOriginalFileDeleted handles refresh.
                // Wait, in old_delete_block, if it deleted rows, it called onOriginalFileDeleted(context) which will flush stagedFiles.
            }

            // We persist the original MediaStore URI and use it directly instead of relying on DATA column path

            // 4. Resolve the ORIGINAL MediaStore URI safely
            var resolvedUri = uri
            if (android.provider.DocumentsContract.isDocumentUri(context, uri) && uri.authority == "com.android.providers.media.documents") {
                try {
                    val docId = android.provider.DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    if (split.size >= 2) {
                        val type = split[0]
                        val id = split[1].toLongOrNull()
                        if (id != null) {
                            val baseUri = when (type) {
                                "image" -> android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                "video" -> android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                "audio" -> android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                else -> android.provider.MediaStore.Files.getContentUri("external")
                            }
                            resolvedUri = android.content.ContentUris.withAppendedId(baseUri, id)
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Failed to resolve SAF URI", e)
                }
            }
            
            // Persist the resolved URI instead of DATA path
            val uriToPersist = resolvedUri.toString()

            // 5. Delete ONLY the original media from MediaStore.
            try {
                if (skipDelete) {
                    // Skip deletion for batch processing
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    if (resolvedUri.authority?.startsWith("media") == true) {
                        android.util.Log.d("Vault", "Delete request created for $resolvedUri")
                        val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                        pendingDeleteOriginalPaths = listOf(uriToPersist)
                        _pendingDeleteSender.value = pendingIntent.intentSender
                    } else {
                        android.util.Log.w("Vault", "Cannot delete non-media URI: $resolvedUri")
                        onOriginalFileDeleted(context)
                    }
                } else {
                    android.util.Log.d("Vault", "Deleting original file directly (API < 30)")
                    val deletedRows = contentResolver.delete(resolvedUri, null, null)
                    if (deletedRows > 0) {
                        android.util.Log.d("Vault", "Delete success/failure: Delete success")
                        pendingDeleteOriginalPaths = listOf(uriToPersist)
                        onOriginalFileDeleted(context)
                    } else {
                        android.util.Log.e("Vault", "Delete success/failure: Delete failure")
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("Vault", "Any exception with full stack trace", e)
            }
            true
        } catch (e: Exception) {
            android.util.Log.e("Vault", "Any exception with full stack trace", e)
            false
        }
    }

    fun deleteVaultFile(fileSerialized: String): Boolean {
        return try {
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            val recentKey = if (isDecoy) "recently_deleted_decoy_files" else "recently_deleted_files"

            // Remove from main vault
            val updatedFiles = _vaultFiles.value - fileSerialized
            _vaultFiles.value = updatedFiles
            prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()

            // Add to Recently Deleted with current timestamp
            val deletionTime = System.currentTimeMillis().toString()
            val recentSerialized = if (fileSerialized.split("|||").size >= 7) {
                fileSerialized
            } else {
                "$fileSerialized|||$deletionTime"
            }

            val savedRecent = prefs.getStringSet(recentKey, emptySet()) ?: emptySet()
            val updatedRecent = savedRecent + recentSerialized
            prefs.edit().putStringSet(recentKey, updatedRecent).apply()

            _recentlyDeletedFiles.value = updatedRecent.toList().sortedByDescending {
                val parts = it.split("|||")
                if (parts.size >= 7) parts[6].toLongOrNull() ?: 0L else 0L
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun restoreFromRecent(recentSerialized: String): Boolean {
        return try {
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            val recentKey = if (isDecoy) "recently_deleted_decoy_files" else "recently_deleted_files"

            // Remove from recently deleted
            val savedRecent = prefs.getStringSet(recentKey, emptySet()) ?: emptySet()
            val updatedRecent = savedRecent - recentSerialized
            prefs.edit().putStringSet(recentKey, updatedRecent).apply()

            _recentlyDeletedFiles.value = updatedRecent.toList().sortedByDescending {
                val parts = it.split("|||")
                if (parts.size >= 7) parts[6].toLongOrNull() ?: 0L else 0L
            }

            // Restore to main vault (strip the 7th part - deletion timestamp)
            val parts = recentSerialized.split("|||")
            val originalSerialized = parts.take(6).joinToString("|||")

            val updatedFiles = _vaultFiles.value + originalSerialized
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deletePermanentlyFromRecent(recentSerialized: String): Boolean {
        return try {
            val isDecoy = _decoyActive.value
            val recentKey = if (isDecoy) "recently_deleted_decoy_files" else "recently_deleted_files"

            // Remove from recently deleted
            val savedRecent = prefs.getStringSet(recentKey, emptySet()) ?: emptySet()
            val updatedRecent = savedRecent - recentSerialized
            prefs.edit().putStringSet(recentKey, updatedRecent).apply()

            _recentlyDeletedFiles.value = updatedRecent.toList().sortedByDescending {
                val parts = it.split("|||")
                if (parts.size >= 7) parts[6].toLongOrNull() ?: 0L else 0L
            }

            // Delete physical file
            val parts = recentSerialized.split("|||")
            if (parts.size >= 5) {
                val absolutePath = parts[4]
                val file = File(absolutePath)
                if (file.exists()) {
                    file.delete()
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @android.annotation.SuppressLint("NewApi")
    fun unhideVaultFile(
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
            val contentValues = android.content.ContentValues().apply {
                put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, originalName)
                put(android.provider.MediaStore.MediaColumns.MIME_TYPE, mimeType)
            }

            var uri: android.net.Uri? = null
            var writeSuccessful = false

            if (mimeType.startsWith("image/")) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    contentValues.put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_PICTURES + "/DCIM")
                }
                uri = resolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            } else if (mimeType.startsWith("video/")) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    contentValues.put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_MOVIES + "/DCIM")
                }
                uri = resolver.insert(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                // Documents & others -> Downloads
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    contentValues.put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
                    uri = resolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                } else {
                    // pre-Q: Save directly to the public Downloads folder via File API
                    val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                    if (!downloadsDir.exists()) {
                        downloadsDir.mkdirs()
                    }
                    var destFile = File(downloadsDir, originalName)
                    if (destFile.exists()) {
                        val baseName = File(originalName).nameWithoutExtension
                        val ext = File(originalName).extension
                        destFile = File(downloadsDir, "${baseName}_${System.currentTimeMillis()}.$ext")
                    }
                    try {
                        file.inputStream().use { input ->
                            FileOutputStream(destFile).use { output ->
                                input.copyTo(output)
                            }
                        }
                        writeSuccessful = true
                        uri = android.net.Uri.fromFile(destFile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            if (uri != null) {
                if (!writeSuccessful) {
                    resolver.openOutputStream(uri)?.use { output ->
                        file.inputStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                }

                // Refresh MediaStore immediately
                android.media.MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    arrayOf(mimeType)
                ) { path, scannedUri ->
                    android.util.Log.d("Vault", "Scanned unhidden file: $path -> $scannedUri")
                }

                // Delete original encrypted file from app's private filesDir
                if (file.exists()) {
                    file.delete()
                }

                // Remove its metadata from main vault
                val updatedFiles = _vaultFiles.value - fileSerialized
                _vaultFiles.value = updatedFiles
                val isDecoy = _decoyActive.value
                val filesKey = if (isDecoy) "decoy_files" else "vault_files"
                prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()

                onSuccess("Media restored successfully.")
            } else {
                onFailure("Failed to create MediaStore entry")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure("Error: ${e.localizedMessage ?: e.toString()}")
        }
    }

    @android.annotation.SuppressLint("NewApi")
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

    fun setIntruderDetectionEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("intruder_detection_enabled", enabled).apply()
        _intruderDetectionEnabled.value = enabled
    }

    fun setAutoLockDuration(seconds: Int) {
        prefs.edit().putInt("auto_lock_duration", seconds).apply()
        _autoLockDuration.value = seconds
    }

    fun setBlurThumbnails(enabled: Boolean) {
        prefs.edit().putBoolean("blur_thumbnails", enabled).apply()
        _blurThumbnails.value = enabled
    }

    fun toggleFolderLock(folderName: String) {
        val current = _lockedFolders.value.toMutableSet()
        if (current.contains(folderName)) {
            current.remove(folderName)
        } else {
            current.add(folderName)
        }
        prefs.edit().putStringSet("locked_folders", current).apply()
        _lockedFolders.value = current
    }

    fun tempUnlockFolder(folderName: String) {
        _tempUnlockedFolders.value = _tempUnlockedFolders.value + folderName
    }

    fun clearTempUnlockedFolders() {
        _tempUnlockedFolders.value = emptySet()
    }

    fun updateLastInteraction() {
        _lastInteractionTime.value = System.currentTimeMillis()
    }

    fun setFileLossProtection(enabled: Boolean) {
        prefs.edit().putBoolean("file_loss_protection", enabled).apply()
        _fileLossProtection.value = enabled
    }

    // --- Private Browser Downloads Engine ---
    private val _downloads = MutableStateFlow<List<DownloadTask>>(emptyList())
    val downloads: StateFlow<List<DownloadTask>> = _downloads.asStateFlow()

    fun clearDownloads() {
        _downloads.value = emptyList()
    }

    fun removeDownload(taskId: String) {
        _downloads.value = _downloads.value.filter { it.id != taskId }
    }

    fun addDownloadedFileToVault(context: Context, filename: String, mimeType: String, bytes: ByteArray): Boolean {
        return try {
            val size = bytes.size.toLong()
            val readableSize = formatFileSize(size)
            val id = System.currentTimeMillis().toString()
            val timestamp = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
            
            val vaultDir = File(context.filesDir, "vault_files")
            if (!vaultDir.exists()) {
                vaultDir.mkdirs()
            }
            
            val ext = File(filename).extension.ifEmpty {
                when {
                    mimeType.startsWith("image/") -> "jpg"
                    mimeType.startsWith("video/") -> "mp4"
                    mimeType.contains("pdf") -> "pdf"
                    mimeType.contains("text") -> "txt"
                    else -> "dat"
                }
            }
            
            val destFileName = "$id.$ext"
            val destFile = File(vaultDir, destFileName)
            destFile.writeBytes(bytes)
            
            val fileSerialized = "$id|||$timestamp|||$filename|||$mimeType|||${destFile.absolutePath}|||$readableSize"
            val updatedFiles = _vaultFiles.value + fileSerialized
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()
            
            true
        } catch (e: Exception) {
            android.util.Log.e("Vault", "Failed to add downloaded file to vault", e)
            false
        }
    }

    fun startVaultDownload(context: Context, url: String, userAgent: String, contentDisposition: String, mimeType: String, contentLength: Long) {
        val taskId = java.util.UUID.randomUUID().toString()
        var filename = android.webkit.URLUtil.guessFileName(url, contentDisposition, mimeType)
        if (filename.isNullOrEmpty() || filename == "downloadfile.bin") {
            val lastPathSegment = Uri.parse(url).lastPathSegment
            if (!lastPathSegment.isNullOrEmpty()) {
                filename = lastPathSegment
            } else {
                filename = "downloaded_file"
            }
        }
        
        val newTask = DownloadTask(
            id = taskId,
            url = url,
            filename = filename,
            progress = 0f,
            status = "Downloading",
            sizeString = formatFileSize(contentLength),
            mimeType = mimeType
        )
        _downloads.value = _downloads.value + newTask
        
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val urlObj = java.net.URL(url)
                val connection = urlObj.openConnection() as java.net.HttpURLConnection
                connection.setRequestProperty("User-Agent", userAgent)
                connection.connect()
                
                if (connection.responseCode in 200..299) {
                    val totalLength = if (contentLength > 0) contentLength else connection.contentLength.toLong()
                    val inputStream = connection.inputStream
                    val outputStream = java.io.ByteArrayOutputStream()
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var totalBytesRead = 0L
                    
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        if (totalLength > 0) {
                            val progressValue = totalBytesRead.toFloat() / totalLength.toFloat()
                            _downloads.value = _downloads.value.map { task ->
                                if (task.id == taskId) task.copy(progress = progressValue) else task
                            }
                        }
                    }
                    
                    inputStream.close()
                    val fileBytes = outputStream.toByteArray()
                    outputStream.close()
                    
                    val finalMime = connection.contentType ?: mimeType
                    val success = addDownloadedFileToVault(context, filename, finalMime, fileBytes)
                    
                    withContext(Dispatchers.Main) {
                        _downloads.value = _downloads.value.map { task ->
                            if (task.id == taskId) {
                                task.copy(
                                    progress = 1f,
                                    status = if (success) "Completed" else "Failed",
                                    mimeType = finalMime
                                )
                            } else task
                        }
                        if (success) {
                            android.widget.Toast.makeText(context, "$filename downloaded directly to Vault!", android.widget.Toast.LENGTH_LONG).show()
                        } else {
                            android.widget.Toast.makeText(context, "Failed to save downloaded file $filename", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _downloads.value = _downloads.value.map { task ->
                            if (task.id == taskId) task.copy(status = "Failed") else task
                        }
                        android.widget.Toast.makeText(context, "HTTP error: ${connection.responseCode}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("VaultDownload", "Error downloading file", e)
                withContext(Dispatchers.Main) {
                    _downloads.value = _downloads.value.map { task ->
                        if (task.id == taskId) task.copy(status = "Failed") else task
                    }
                    android.widget.Toast.makeText(context, "Download error: ${e.localizedMessage}", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun registerDirectVaultFile(context: Context, file: File, originalName: String, mimeType: String) {
        val size = file.length()
        val readableSize = formatFileSize(size)
        val id = System.currentTimeMillis().toString()
        val timestamp = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        
        val fileSerialized = "$id|||$timestamp|||$originalName|||$mimeType|||${file.absolutePath}|||$readableSize"
        val updatedFiles = _vaultFiles.value + fileSerialized
        _vaultFiles.value = updatedFiles.sortedByDescending { it }
        
        val isDecoy = _decoyActive.value
        val filesKey = if (isDecoy) "decoy_files" else "vault_files"
        prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()
    }
}

data class DownloadTask(
    val id: String,
    val url: String,
    val filename: String,
    val progress: Float, // 0.0f to 1.0f
    val status: String, // "Downloading", "Completed", "Failed"
    val sizeString: String = "0 B",
    val mimeType: String = ""
)

data class RecentItem(
    val id: String,
    val type: String,
    val name: String,
    val timestamp: Long,
    val extra: String = ""
)

data class StorageDetails(
    val photosSize: Long,
    val photosCount: Int,
    val videosSize: Long,
    val videosCount: Int,
    val documentsSize: Long,
    val documentsCount: Int,
    val notesSize: Long,
    val notesCount: Int,
    val totalSize: Long
)

