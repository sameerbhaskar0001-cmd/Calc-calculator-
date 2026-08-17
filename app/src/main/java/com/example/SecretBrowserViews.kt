package com.example

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.animation.core.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.*
import androidx.compose.material3.ripple
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.flow.collectLatest

// BRAND & COLORS CONSTANTS
val LightBg = Color(0xFFF8F9FA)
val LightCard = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF111111)
val TextSecondary = Color(0xFF666666)
val BorderColor = Color(0xFFE8E8E8)
val AccentColor = Color(0xFFFF6A00)
val DangerColor = Color(0xFFC62828)
val SuccessColor = Color(0xFF2E7D32)

@Composable
fun Modifier.premiumPressClick(
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "PremiumScale"
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(color = AccentColor.copy(alpha = 0.15f)),
            onClick = onClick
        )
}

@Composable
fun SecretBrowserHome(
    tabs: List<TabState>,
    activeTabId: String,
    searchEngine: String,
    browserBookmarks: List<BrowserBookmark>,
    browserHistory: List<BrowserHistory>,
    onSearch: (String) -> Unit,
    onOpenNewTab: (String) -> Unit,
    onSelectActiveTab: (String) -> Unit,
    onCloseTab: (String) -> Unit,
    onShowBookmarks: () -> Unit,
    onShowHistory: () -> Unit,
    onShowDownloads: () -> Unit,
    onShowSettings: () -> Unit,
    onShowSearchEngineDialog: () -> Unit,
    onClearAllData: () -> Unit
) {
    val context = LocalContext.current
    var searchInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 1. TOP MINIMAL LOGO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(LightCard, RoundedCornerShape(22.dp))
                    .border(1.5.dp, BorderColor, RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Secret Browser Logo",
                    tint = AccentColor,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Secret Browser",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Private. Independent. Secure.",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. LARGE FLOATING SEARCH CAPSULE
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(31.dp),
            colors = CardDefaults.cardColors(containerColor = LightCard),
            border = BorderStroke(1.dp, BorderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(LightBg)
                        .premiumPressClick { onShowSearchEngineDialog() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = AccentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = searchEngine,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (searchInput.isEmpty()) {
                        Text("Search or enter URL...", color = TextSecondary, fontSize = 14.sp)
                    }
                    BasicTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        singleLine = true,
                        textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchInput.trim().isNotEmpty()) {
                                    onSearch(searchInput.trim())
                                }
                            }
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AccentColor)
                        .premiumPressClick {
                            if (searchInput.trim().isNotEmpty()) {
                                onSearch(searchInput.trim())
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Go",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 3. QUICK ACTIONS
        Text(
            text = "QUICK ACTIONS",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .premiumPressClick { onOpenNewTab("home") },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, "New Tab", tint = AccentColor, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("New Tab", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Isolated session", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .premiumPressClick { onShowBookmarks() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, "Bookmarks", tint = AccentColor, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Bookmarks", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Access saved", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .premiumPressClick { onClearAllData() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(DangerColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DeleteForever, "Wipe", tint = DangerColor, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Destruct", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Purge all data", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .premiumPressClick { onShowSettings() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Settings, "Settings", tint = AccentColor, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Settings", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Configure engine", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 4. RECENT ACTIVE TABS
        Text(
            text = "RECENT ACTIVE TABS",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        val recentTabs = tabs.filter { it.id != activeTabId }
        if (recentTabs.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🌐",
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Active Tabs",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Start a secure browsing session.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onOpenNewTab("home") },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("New Tab", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(recentTabs) { tab ->
                    Card(
                        modifier = Modifier
                            .width(220.dp)
                            .premiumPressClick { onSelectActiveTab(tab.id) },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCard),
                        border = BorderStroke(1.dp, BorderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(LightBg, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = "Tab icon",
                                        tint = AccentColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (tab.url == "home") "New Tab" else tab.title,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = if (tab.url == "home") "Internal start page" else tab.url,
                                        color = TextSecondary,
                                        fontSize = 10.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            IconButton(
                                onClick = { onCloseTab(tab.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Tab",
                                    tint = DangerColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 5. SECURE COLLECTIONS
        Text(
            text = "SECURE COLLECTIONS",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.weight(1f).height(72.dp).premiumPressClick { onShowBookmarks() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, "Bookmarks", tint = AccentColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Bookmarks", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).height(72.dp).premiumPressClick { onShowDownloads() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, "Downloads", tint = AccentColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Downloads", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.weight(1f).height(72.dp).premiumPressClick { onShowHistory() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, "History", tint = AccentColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("History Logs", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).height(72.dp).premiumPressClick {
                        Toast.makeText(context, "Added current pages to Reading Later queue", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.InsertDriveFile, "Reading Later", tint = AccentColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Reading Later", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(110.dp)) // Avoid content covered by dock bottom bar
    }
}

@Composable
fun SecretBrowserSettingsDashboard(
    tabs: List<TabState>,
    browserBookmarks: List<BrowserBookmark>,
    browserHistory: List<BrowserHistory>,
    searchEngine: String,
    savePasswords: Boolean,
    clearHistoryOnExit: Boolean,
    clearTempOnExit: Boolean,
    useGeckoView: Boolean,
    trackingProtectionEnabled: Boolean,
    onSetTrackingProtectionEnabled: (Boolean) -> Unit,
    totalTrackersBlocked: Int,
    onBack: () -> Unit,
    onShowSearchEngineDialog: () -> Unit,
    onSetSavePasswords: (Boolean) -> Unit,
    onSetClearHistoryOnExit: (Boolean) -> Unit,
    onSetClearTempOnExit: (Boolean) -> Unit,
    onClearBrowsingData: (clearHistory: Boolean, clearCookies: Boolean, clearCache: Boolean, clearSiteData: Boolean, onResult: (Boolean) -> Unit) -> Unit,
    onShowDownloads: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showClearTempFilesDialog by remember { mutableStateOf(false) }
    var showClearBrowsingDataDialog by remember { mutableStateOf(false) }
    var clearHist by remember { mutableStateOf(true) }
    var clearCooks by remember { mutableStateOf(true) }
    var clearCach by remember { mutableStateOf(true) }
    var clearSite by remember { mutableStateOf(true) }

    if (showClearBrowsingDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearBrowsingDataDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = DangerColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Clear browsing data?", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Selected browsing data will be permanently removed.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Checkbox for History
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { clearHist = !clearHist }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = clearHist,
                            onCheckedChange = { clearHist = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("History", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Clear saved browser activity logs", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    // Checkbox for Cookies
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { clearCooks = !clearCooks }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = clearCooks,
                            onCheckedChange = { clearCooks = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Cookies", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Sign out of active web sessions", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    // Checkbox for Cache
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { clearCach = !clearCach }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = clearCach,
                            onCheckedChange = { clearCach = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Cache", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Clear rendering files and temp assets", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    // Checkbox for Site Data
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { clearSite = !clearSite }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = clearSite,
                            onCheckedChange = { clearSite = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Site Data", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Clear local storage and site settings", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            onClearBrowsingData(true, true, true, true) { success ->
                                if (success) {
                                    Toast.makeText(context, "All browsing data cleared successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Some browsing data failed to clear", Toast.LENGTH_SHORT).show()
                                }
                            }
                            showClearBrowsingDataDialog = false
                        }
                    ) {
                        Text("Clear All", color = TextSecondary)
                    }

                    Button(
                        onClick = {
                            if (!clearHist && !clearCooks && !clearCach && !clearSite) {
                                Toast.makeText(context, "No categories selected", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            onClearBrowsingData(clearHist, clearCooks, clearCach, clearSite) { success ->
                                if (success) {
                                    Toast.makeText(context, "Selected data cleared successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to clear some selected data", Toast.LENGTH_SHORT).show()
                                }
                            }
                            showClearBrowsingDataDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DangerColor)
                    ) {
                        Text("Clear", color = Color.White)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearBrowsingDataDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = LightCard,
            tonalElevation = 6.dp
        )
    }

    if (showClearTempFilesDialog) {
        AlertDialog(
            onDismissRequest = { showClearTempFilesDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = DangerColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Clear Temporary Files?", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    "Are you sure you want to securely clear all temporary, incomplete-download, and upload files from the browser? This action is permanent.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearTempFilesDialog = false
                        coroutineScope.launch(Dispatchers.IO) {
                            val ok1 = SecretBrowserSecureDelete.cleanTemporaryUploadsDirectory(context, secure = true)
                            val ok2 = SecretBrowserSecureDelete.cleanStaleTemporaryRemnants(context, secure = true)
                            withContext(Dispatchers.Main) {
                                if (ok1 && ok2) {
                                    Toast.makeText(context, "Temporary files securely cleared successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Temporary files cleared (some active/locked files skipped)", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerColor)
                ) {
                    Text("Clear Securely", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearTempFilesDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = LightCard,
            tonalElevation = 6.dp
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("Secret Browser", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Configuration & Security Dashboard", color = TextSecondary, fontSize = 12.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // STATS SECTION (DASHBOARD STYLE)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ENGINE STATISTICS",
                        color = AccentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = tabs.size.toString(), color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Active Tabs", color = TextSecondary, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(32.dp).background(BorderColor))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = browserBookmarks.size.toString(), color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Bookmarks", color = TextSecondary, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(32.dp).background(BorderColor))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = browserHistory.size.toString(), color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = "History Logs", color = TextSecondary, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(32.dp).background(BorderColor))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = totalTrackersBlocked.toString(), color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Blocked", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }

            // SECURITY STATUS BADGES CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SECURITY SHIELD STATUS",
                        color = SuccessColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CheckCircle, "Active", tint = SuccessColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Isolated Sandbox Cookies", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Independent cookie containers prevent cross-site correlation", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CheckCircle, "Active", tint = SuccessColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Kernel Separation Sandbox", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Local files and OS layers completely hidden from renderers", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val activeColor = if (trackingProtectionEnabled) SuccessColor else TextSecondary.copy(alpha = 0.5f)
                        val activeText = if (trackingProtectionEnabled) "Active Shielding" else "Shielding Disabled"
                        Icon(if (trackingProtectionEnabled) Icons.Default.CheckCircle else Icons.Default.Cancel, "Status", tint = activeColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Real-Time $activeText", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Block tracking networks, cross-site beacons, and ads", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }

            // GENERAL CONFIGURATIONS
            Text(
                text = "GENERAL SETTINGS",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .premiumPressClick { onShowSearchEngineDialog() }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Search, "Search", tint = AccentColor, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Search Engine", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text(searchEngine, color = AccentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Icon(Icons.Default.ArrowDropDown, "Dropdown", tint = TextSecondary)
                    }
                }
            }

            // PRIVACY CONTROLS
            Text(
                text = "PRIVACY & DATA DESTRUCTION",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(SuccessColor.copy(alpha = 0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Security, "Shield", tint = SuccessColor, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Tracking Protection", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Block scripts, beacons, and trackers", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = trackingProtectionEnabled,
                            onCheckedChange = onSetTrackingProtectionEnabled,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SuccessColor)
                        )
                    }

                    HorizontalDivider(color = BorderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(TextSecondary.copy(alpha = 0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, "Lock", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Save Passwords", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Save credential states inside forms", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = savePasswords,
                            onCheckedChange = onSetSavePasswords,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AccentColor)
                        )
                    }

                    HorizontalDivider(color = BorderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(TextSecondary.copy(alpha = 0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.History, "History", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Clear History on Exit", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Automatically purge logs upon exit", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = clearHistoryOnExit,
                            onCheckedChange = onSetClearHistoryOnExit,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AccentColor)
                        )
                    }

                    HorizontalDivider(color = BorderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(TextSecondary.copy(alpha = 0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Delete, "Delete", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Clear Temporary Browser Files on Exit", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Automatically purge temporary uploads and partial downloads on exit", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = clearTempOnExit,
                            onCheckedChange = onSetClearTempOnExit,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AccentColor)
                        )
                    }

                    HorizontalDivider(color = BorderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showClearBrowsingDataDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(DangerColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DeleteForever, "Clear Browsing Data", tint = DangerColor, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Clear Browsing Data", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("History, cookies, cache, and site storage", color = TextSecondary, fontSize = 11.sp)
                        }
                        Text(
                            text = "Clear",
                            color = DangerColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(color = BorderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showClearTempFilesDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(DangerColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DeleteForever, "Clear Temp", tint = DangerColor, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Clear Temporary Browser Files", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Securely clear upload, download remnants, and browser cache files", color = TextSecondary, fontSize = 11.sp)
                        }
                        Text(
                            text = "Clear",
                            color = DangerColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }

            // ENGINE PREFERENCE
            Text(
                text = "SANDBOX ENGINE",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(SuccessColor.copy(alpha = 0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Security, "Engine", tint = SuccessColor, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("GeckoView Engine", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text(if (useGeckoView) "Currently active high-privacy renderer" else "WebKit renderer currently active", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(SuccessColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("STABLE", color = SuccessColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // NEXT GEN CAPABILITIES
            Text(
                text = "NEXT-GEN PROTECTION (COMING SOON)",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Shield",
                                tint = AccentColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Decentralized Onion Routing", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Multi-hop encrypted proxy relays inside your tabs", color = TextSecondary, fontSize = 11.sp)
                        }
                        Box(
                            modifier = Modifier
                                .background(AccentColor.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("COMING SOON", color = AccentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock",
                                tint = AccentColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AI Fingerprint Shield", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Neural traffic blocks preventing advanced data extraction", color = TextSecondary, fontSize = 11.sp)
                        }
                        Box(
                            modifier = Modifier
                                .background(AccentColor.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("COMING SOON", color = AccentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SecretBrowserBookmarksScreen(
    browserBookmarks: List<BrowserBookmark>,
    onBack: () -> Unit,
    onSelectBookmark: (String) -> Unit,
    onDeleteBookmark: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary)
            }
            Text("Bookmarks", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (browserBookmarks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, "No bookmarks", tint = AccentColor, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No bookmarks yet", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Bookmark your favorite pages to access them quickly from here.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(browserBookmarks) { bookmark ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCard),
                        border = BorderStroke(1.dp, BorderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectBookmark(bookmark.url) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bookmark.title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(bookmark.url, color = TextSecondary, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            IconButton(onClick = { onDeleteBookmark(bookmark.url) }) {
                                Icon(Icons.Default.Delete, "Delete", tint = DangerColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecretBrowserHistoryScreen(
    browserHistory: List<BrowserHistory>,
    onBack: () -> Unit,
    onSelectHistoryItem: (String) -> Unit,
    onClearHistory: () -> Unit,
    onDeleteHistoryItem: (BrowserHistory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary)
                }
                Text("History Logs", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
            if (browserHistory.isNotEmpty()) {
                IconButton(onClick = onClearHistory) {
                    Icon(Icons.Default.DeleteForever, "Clear History", tint = DangerColor)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (browserHistory.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(AccentColor.copy(alpha = 0.08f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.History, "No history", tint = AccentColor, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No history yet", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Pages you visit during your session will appear here.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(browserHistory) { historyItem ->
                    val date = Date(historyItem.timestamp)
                    val formatter = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCard),
                        border = BorderStroke(1.dp, BorderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectHistoryItem(historyItem.url) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(historyItem.title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(historyItem.url, color = TextSecondary, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                                    Text(formatter.format(date), color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                            IconButton(onClick = { onDeleteHistoryItem(historyItem) }) {
                                Icon(Icons.Default.Delete, "Delete", tint = DangerColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateBrowserSection(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onExit: () -> Unit = {},
    onPanic: () -> Unit = {}
) {
    val context = LocalContext.current
    val tabs = viewModel.browserTabs
    val webViews = remember { mutableStateMapOf<String, android.webkit.WebView>() }
    val geckoSessions = remember { 
        mutableStateMapOf<String, org.mozilla.geckoview.GeckoSession>().apply {
            putAll(GeckoSessionManager.getActiveSessions())
        }
    }
    var activeTabId by remember { mutableStateOf<String?>(viewModel.activeTabId) }

    LaunchedEffect(viewModel.activeTabId) {
        if (viewModel.activeTabId != null && activeTabId != viewModel.activeTabId) {
            activeTabId = viewModel.activeTabId
        }
    }

    var showFindInPage by remember { mutableStateOf(false) }
    var findInPageText by remember { mutableStateOf("") }
    var findInPageMatchCurrent by remember { mutableStateOf(0) }
    var findInPageMatchTotal by remember { mutableStateOf(0) }

    LaunchedEffect(activeTabId) {
        if (activeTabId != null && viewModel.activeTabId != activeTabId) {
            viewModel.activeTabId = activeTabId
        }
        // Clear Find in Page highlights and close search when tab switches
        showFindInPage = false
        findInPageText = ""
        findInPageMatchCurrent = 0
        findInPageMatchTotal = 0
    }
    
    LaunchedEffect(tabs.toList(), activeTabId) {
        if (activeTabId == null && tabs.isNotEmpty()) {
            activeTabId = viewModel.activeTabId ?: tabs.first().id
        }
        if (viewModel.isBrowserTabsLoaded) {
            viewModel.triggerSaveTabs()
        }
    }

    var showTabSwitcher by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showDownloads by remember { mutableStateOf(false) }
    var showBookmarks by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var isEditingUrl by remember { mutableStateOf(false) }
    var editingUrlText by remember { mutableStateOf("") }

    val browserBookmarks by viewModel.browserBookmarks.collectAsStateWithLifecycle()
    val browserHistory by viewModel.browserHistory.collectAsStateWithLifecycle()
    val searchEngine by viewModel.searchEngine.collectAsStateWithLifecycle()
    val savePasswords by viewModel.savePasswords.collectAsStateWithLifecycle()
    val clearHistoryOnExit by viewModel.clearHistoryOnExit.collectAsStateWithLifecycle()
    val clearTempOnExit by viewModel.clearTempOnExit.collectAsStateWithLifecycle()
    val useGeckoView by viewModel.useGeckoView.collectAsStateWithLifecycle()
    val trackingProtectionEnabled by viewModel.trackingProtectionEnabled.collectAsStateWithLifecycle()
    val trackingSiteEnabled by viewModel.trackingSiteEnabled.collectAsStateWithLifecycle()
    val trackingSiteDisabled by viewModel.trackingSiteDisabled.collectAsStateWithLifecycle()
    val totalTrackersBlocked by viewModel.totalTrackersBlocked.collectAsStateWithLifecycle()

    var showSearchEngineDialog by remember { mutableStateOf(false) }
    var activePopupWebView by remember { mutableStateOf<android.webkit.WebView?>(null) }
    var pendingDownload by remember { mutableStateOf<PendingDownloadData?>(null) }
    var showSiteSecurityDialog by remember { mutableStateOf(false) }

    val activeTab = tabs.find { it.id == activeTabId } ?: tabs.find { it.id == viewModel.activeTabId } ?: tabs.firstOrNull()

    LaunchedEffect(activeTab?.isFullScreen) {
        val activity = context as? android.app.Activity ?: return@LaunchedEffect
        if (activeTab?.isFullScreen == true) {
            activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            setSystemBarsVisibility(activity, false)
        } else {
            activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            setSystemBarsVisibility(activity, true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.memoryPressureEvent.collectLatest { level ->
            if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
                // Free up RAM memory for WebView fallback instances (leaves disk cache intact)
                webViews.values.forEach { webView ->
                    try {
                        webView.clearCache(false)
                    } catch (e: Exception) {
                        android.util.Log.e("SecretBrowser", "Failed to clear WebView memory", e)
                    }
                }
                
                try {
                    activePopupWebView?.clearCache(false)
                } catch (e: Exception) {
                    android.util.Log.e("SecretBrowser", "Failed to clear popup WebView memory", e)
                }
                
                // GeckoRuntime internally registers its ComponentCallbacks2 to manage its memory safely.
                // We do not force-close GeckoSessions as that destroys user navigation state.
                android.util.Log.i("SecretBrowser", "Low memory signal received (level $level). Cleared WebView RAM caches safely. Preserved GeckoSessions.")
            }
        }
    }

    LaunchedEffect(activeTabId, geckoSessions.size) {
        geckoSessions.forEach { (id, session) ->
            try {
                session.setActive(id == activeTabId)
            } catch (e: Exception) {
                android.util.Log.e("GeckoActiveState", "Failed to set active state for $id", e)
            }
        }
    }

    LaunchedEffect(activeTabId, webViews.size) {
        webViews.forEach { (id, webView) ->
            try {
                if (id == activeTabId) {
                    webView.onResume()
                } else {
                    webView.onPause()
                }
            } catch (e: Exception) {
                android.util.Log.e("WebViewActiveState", "Failed to set active state for $id", e)
            }
        }
    }

    LaunchedEffect(tabs.map { it.id }, activeTabId, useGeckoView) {
        val currentTabIds = tabs.map { it.id }.toSet()

        if (useGeckoView) {
            // Safe clean up of fallback WebView resources on transition
            webViews.values.forEach { webView ->
                try {
                    webView.stopLoading()
                    (webView.parent as? android.view.ViewGroup)?.removeView(webView)
                    webView.webViewClient = android.webkit.WebViewClient()
                    webView.webChromeClient = android.webkit.WebChromeClient()
                    webView.setDownloadListener(null)
                    webView.clearHistory()
                    webView.clearCache(true)
                    webView.loadUrl("about:blank")
                    webView.destroy()
                } catch (e: Exception) {}
            }
            webViews.clear()
        } else {
            // Safe clean up of primary GeckoView resources on transition
            geckoSessions.keys.forEach { tabId ->
                GeckoSessionManager.removeAndDestroySession(tabId)
            }
            geckoSessions.clear()
        }

        // Clean up closed tabs
        val removedGecko = geckoSessions.keys.filter { it !in currentTabIds }
        removedGecko.forEach { GeckoSessionManager.removeAndDestroySession(it) }
        geckoSessions.keys.retainAll(currentTabIds)
        
        val removedWebViews = webViews.keys.filter { it !in currentTabIds }
        removedWebViews.forEach { webViews[it]?.destroy() }
        webViews.keys.retainAll(currentTabIds)

        // Only initialize the active tab to optimize startup and memory
        val activeTab = tabs.find { it.id == activeTabId }
        if (activeTab != null) {
            val tab = activeTab
            if (useGeckoView) {
                if (!geckoSessions.containsKey(tab.id)) {
                    val session = createPrivateGeckoSession(
                        ctx = context,
                        tabId = tab.id,
                        initialUrl = tab.url,
                        isDesktopMode = tab.isDesktopMode,
                        onDownloadRequested = { downloadUrl, userAgent, contentDisposition, mimeType, contentLength ->
                            pendingDownload = PendingDownloadData(downloadUrl, userAgent, contentDisposition, mimeType, contentLength)
                        },
                        onCrash = {
                            // Remove session from compose map to trigger recreation in next composition
                            geckoSessions.remove(tab.id)
                        }
                    ) { transform ->
                        val index = tabs.indexOfFirst { it.id == tab.id }
                        if (index != -1) {
                            val oldTab = tabs[index]
                            val newTab = transform(oldTab)
                            tabs[index] = newTab
                            val currentUrl = newTab.url
                            val currentTitle = newTab.title
                            if (!newTab.isLoading && oldTab.isLoading) {
                                if (currentUrl != "home" && currentUrl != "about:blank" && currentUrl.isNotEmpty() &&
                                    !currentUrl.startsWith("data:") && !currentUrl.startsWith("file:") && !currentUrl.startsWith("about:")) {
                                    viewModel.addBrowserHistory(currentTitle, currentUrl)
                                }
                            }
                        }
                    }
                    geckoSessions[tab.id] = session
                }
            } else {
                if (!webViews.containsKey(tab.id)) {
                    val webView = createPrivateWebView(
                        ctx = context,
                        tabId = tab.id,
                        initialUrl = tab.url,
                        savePasswords = savePasswords,
                        isDesktopMode = tab.isDesktopMode,
                        onDownloadRequested = { downloadUrl, userAgent, contentDisposition, mimeType, contentLength ->
                            pendingDownload = PendingDownloadData(downloadUrl, userAgent, contentDisposition, mimeType, contentLength)
                        },
                        onCreatePopup = { activePopupWebView = it },
                        onShowCustomView = { view, callback ->
                            val activity = context as? android.app.Activity
                            if (activity != null) {
                                val decor = activity.window.decorView as? android.widget.FrameLayout
                                if (decor != null) {
                                    try { (view.parent as? android.view.ViewGroup)?.removeView(view) } catch(e: Exception) {}
                                    view.setBackgroundColor(android.graphics.Color.BLACK)
                                    view.layoutParams = android.widget.FrameLayout.LayoutParams(
                                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                                    )
                                    decor.addView(view)
                                    view.requestFocus()
                                    viewModel.browserCustomView = view
                                    viewModel.browserCustomViewCallback = callback
                                    activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                                    setSystemBarsVisibility(activity, false)
                                    // Update state
                                    val index = tabs.indexOfFirst { it.id == tab.id }
                                    if (index != -1) tabs[index] = tabs[index].copy(isFullScreen = true)
                                }
                            }
                        },
                        onHideCustomView = {
                            val viewToRemove = viewModel.browserCustomView
                            if (viewToRemove != null) {
                                try { (viewToRemove.parent as? android.view.ViewGroup)?.removeView(viewToRemove) } catch(e: Exception) {}
                            }
                            viewModel.browserCustomView = null
                            viewModel.browserCustomViewCallback = null
                            val activity = context as? android.app.Activity
                            activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            setSystemBarsVisibility(activity, true)
                            // Update state
                            val index = tabs.indexOfFirst { it.id == tab.id }
                            if (index != -1) tabs[index] = tabs[index].copy(isFullScreen = false)
                        },
                        onCrash = {
                            webViews.remove(tab.id)
                        }
                    ) { transform ->
                        val index = tabs.indexOfFirst { it.id == tab.id }
                        if (index != -1) {
                            val oldTab = tabs[index]
                            val newTab = transform(oldTab)
                            tabs[index] = newTab
                            val currentUrl = newTab.url
                            val currentTitle = newTab.title
                            if (!newTab.isLoading && oldTab.isLoading) {
                                if (currentUrl != "home" && currentUrl != "about:blank" && currentUrl.isNotEmpty() &&
                                    !currentUrl.startsWith("data:") && !currentUrl.startsWith("file:") && !currentUrl.startsWith("about:")) {
                                    viewModel.addBrowserHistory(currentTitle, currentUrl)
                                }
                            }
                        }
                    }
                    webViews[tab.id] = webView
                }
            }
        }
    }

    val openNewTab: (String) -> Unit = { url ->
        val tabId = java.util.UUID.randomUUID().toString()
        val newTab = TabState(id = tabId, url = url, title = "New Tab")
        tabs.add(newTab)
        activeTabId = tabId
    }

    val closeTab: (String) -> Unit = { tabId ->
        GeckoSessionManager.removeAndDestroySession(tabId)
        geckoSessions.remove(tabId)
        val wv = webViews[tabId]
        if (wv != null) {
            try {
                wv.stopLoading()
                (wv.parent as? android.view.ViewGroup)?.removeView(wv)
                wv.webViewClient = android.webkit.WebViewClient()
                wv.webChromeClient = android.webkit.WebChromeClient()
                wv.setDownloadListener(null)
                wv.clearHistory()
                wv.clearCache(true)
                wv.loadUrl("about:blank")
                wv.destroy()
            } catch (e: Exception) {}
            webViews.remove(tabId)
        }
        val tIndex = tabs.indexOfFirst { it.id == tabId }
        if (tIndex != -1) {
            tabs.removeAt(tIndex)
            if (activeTabId == tabId) {
                if (tabs.isNotEmpty()) {
                    activeTabId = tabs[tIndex.coerceAtMost(tabs.size - 1)].id
                } else {
                    openNewTab("home")
                }
            }
        }
    }

    LaunchedEffect(viewModel.isBrowserTabsLoaded) {
        if (viewModel.isBrowserTabsLoaded && tabs.isEmpty()) {
            openNewTab("home")
        }
    }

    val currentClearHistoryOnExit by androidx.compose.runtime.rememberUpdatedState(clearHistoryOnExit)
    val currentClearTempOnExit by androidx.compose.runtime.rememberUpdatedState(clearTempOnExit)
    DisposableEffect(Unit) {
        onDispose {
            if (currentClearTempOnExit) {
                try {
                    SecretBrowserSecureDelete.cleanTemporaryUploadsDirectory(context, secure = true)
                    SecretBrowserSecureDelete.cleanStaleTemporaryRemnants(context, secure = true)
                } catch (e: Exception) {
                    android.util.Log.e("SecureDelete", "Exit cleanup failed", e)
                }
            }
            if (currentClearHistoryOnExit) {
                viewModel.clearBrowserHistory()
                clearAllBrowsingData(context, tabs, webViews)
                GeckoSessionManager.destroyAllSessions()
            } else {
                webViews.values.forEach { webView ->
                    try {
                        (webView.parent as? android.view.ViewGroup)?.removeView(webView)
                        webView.destroy()
                    } catch (e: Exception) {}
                }
                webViews.clear()
                geckoSessions.values.forEach { it.setActive(false) }
            }
            geckoSessions.clear()
        }
    }

    val activeWebView = webViews[activeTabId]
    val activeGeckoSession = geckoSessions[activeTabId]
    val isHome = activeTab?.url == "home" || activeTab?.url == "about:blank" || activeTab?.url?.isEmpty() == true

    val currentUrl = activeTab?.url ?: ""
    val currentHost = remember(currentUrl) {
        if (currentUrl.isNotEmpty() && currentUrl != "home" && currentUrl != "about:blank") {
            try {
                val uri = android.net.Uri.parse(currentUrl)
                uri.host
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    val siteOverride = remember(currentHost, trackingSiteEnabled, trackingSiteDisabled) {
        currentHost?.let { host ->
            val norm = host.lowercase().trim()
            if (trackingSiteEnabled.contains(norm)) {
                true
            } else if (trackingSiteDisabled.contains(norm)) {
                false
            } else {
                null
            }
        }
    }
    val isProtectionActive = siteOverride ?: trackingProtectionEnabled

    val performFindInPage: (String, Boolean) -> Unit = { query, forward ->
        if (activeGeckoSession != null) {
            val finder = activeGeckoSession.getFinder()
            if (query.isEmpty()) {
                finder.clear()
                findInPageMatchCurrent = 0
                findInPageMatchTotal = 0
            } else {
                val flags = if (forward) {
                    0
                } else {
                    org.mozilla.geckoview.GeckoSession.FINDER_FIND_BACKWARDS
                }
                finder.find(query, flags).accept { result ->
                    if (result != null) {
                        findInPageMatchCurrent = result.current
                        findInPageMatchTotal = result.total
                    } else {
                        findInPageMatchCurrent = 0
                        findInPageMatchTotal = 0
                    }
                }
            }
        } else if (activeWebView != null) {
            val webView = activeWebView
            if (query.isEmpty()) {
                webView.clearMatches()
                findInPageMatchCurrent = 0
                findInPageMatchTotal = 0
            } else {
                webView.setFindListener { activeMatchOrdinal, numberOfMatches, isDoneCounting ->
                    if (numberOfMatches > 0) {
                        findInPageMatchCurrent = activeMatchOrdinal + 1
                        findInPageMatchTotal = numberOfMatches
                    } else {
                        findInPageMatchCurrent = 0
                        findInPageMatchTotal = 0
                    }
                }
                if (forward) {
                    webView.findAllAsync(query)
                } else {
                    webView.findNext(false)
                }
            }
        }
    }

    val findNextMatch: () -> Unit = {
        val query = findInPageText
        if (query.isNotEmpty()) {
            if (activeGeckoSession != null) {
                activeGeckoSession.getFinder().find(query, 0).accept { result ->
                    if (result != null) {
                        findInPageMatchCurrent = result.current
                        findInPageMatchTotal = result.total
                    } else {
                        findInPageMatchCurrent = 0
                        findInPageMatchTotal = 0
                    }
                }
            } else if (activeWebView != null) {
                activeWebView.findNext(true)
            }
        }
    }

    val findPreviousMatch: () -> Unit = {
        val query = findInPageText
        if (query.isNotEmpty()) {
            if (activeGeckoSession != null) {
                activeGeckoSession.getFinder().find(query, org.mozilla.geckoview.GeckoSession.FINDER_FIND_BACKWARDS).accept { result ->
                    if (result != null) {
                        findInPageMatchCurrent = result.current
                        findInPageMatchTotal = result.total
                    } else {
                        findInPageMatchCurrent = 0
                        findInPageMatchTotal = 0
                    }
                }
            } else if (activeWebView != null) {
                activeWebView.findNext(false)
            }
        }
    }

    val closeFindInPage: () -> Unit = {
        showFindInPage = false
        findInPageText = ""
        findInPageMatchCurrent = 0
        findInPageMatchTotal = 0
        if (activeGeckoSession != null) {
            activeGeckoSession.getFinder().clear()
        } else if (activeWebView != null) {
            activeWebView.clearMatches()
        }
    }

    val stopLoading: () -> Unit = {
        if (activeGeckoSession != null) {
            activeGeckoSession.stop()
        } else {
            activeWebView?.stopLoading()
        }
    }

    val loadUrl: (String) -> Unit = { target ->
        val formatted = if (target == "home" || target == "about:blank") {
            target
        } else {
            val q = target.trim()
            if (q.startsWith("http://") || q.startsWith("https://") || q.startsWith("file://") || q.startsWith("about:")) {
                q
            } else {
                val hasSpace = q.contains(" ")
                val firstSegment = q.substringBefore("/")
                val hasDot = firstSegment.contains(".")
                val isLocalhost = firstSegment.lowercase() == "localhost" || firstSegment.lowercase().startsWith("localhost:" )
                val isValidWebUrl = !hasSpace && (hasDot || isLocalhost)
                if (isValidWebUrl) {
                    "https://$q"
                } else {
                    val encodedQ = java.net.URLEncoder.encode(q, "UTF-8")
                    when (searchEngine) {
                        "DuckDuckGo" -> "https://duckduckgo.com/?q=$encodedQ&kl=us-en"
                        "Bing" -> "https://www.bing.com/search?q=$encodedQ&setlang=en&cc=US"
                        "Yahoo" -> "https://search.yahoo.com/search?p=$encodedQ&ei=UTF-8&vc=US&vl=en"
                        else -> "https://www.google.com/search?q=$encodedQ&hl=en&gl=US"
                    }
                }
            }
        }

        if (formatted == "home") {
            val index = tabs.indexOfFirst { it.id == activeTabId }
            if (index != -1) {
                tabs[index] = tabs[index].copy(
                    url = "home",
                    title = "New Tab",
                    progress = 0,
                    isLoading = false,
                    canGoBack = activeTab?.canGoBack == true || activeWebView?.canGoBack() == true,
                    canGoForward = activeTab?.canGoForward == true || activeWebView?.canGoForward() == true
                )
            }
            if (activeGeckoSession != null) {
                activeGeckoSession.loadUri("about:blank")
            } else {
                activeWebView?.loadUrl("about:blank")
            }
        } else {
            val index = tabs.indexOfFirst { it.id == activeTabId }
            if (index != -1) {
                tabs[index] = tabs[index].copy(url = formatted)
            }
            if (activeGeckoSession != null) {
                activeGeckoSession.loadUri(formatted)
            } else {
                activeWebView?.loadUrl(formatted)
            }
        }
    }

    val reload: () -> Unit = {
        if (activeGeckoSession != null) {
            activeGeckoSession.reload()
        } else {
            activeWebView?.reload()
        }
    }
    val goBack: () -> Unit = {
        if (activeTab?.url == "home") {
            if (activeGeckoSession != null && activeTab.canGoBack) {
                activeGeckoSession.goBack()
            } else if (activeWebView != null && activeWebView.canGoBack()) {
                activeWebView.goBack()
            }
        } else {
            if (activeGeckoSession != null) {
                activeGeckoSession.goBack()
            } else {
                activeWebView?.goBack()
            }
        }
    }
    val goForward: () -> Unit = {
        if (activeGeckoSession != null) {
            activeGeckoSession.goForward()
        } else {
            activeWebView?.goForward()
        }
    }

    val goBackOrExit = {
        if (showSearchEngineDialog) {
            showSearchEngineDialog = false
        } else if (showDownloads) {
            showDownloads = false
        } else if (showSettings) {
            showSettings = false
        } else if (showBookmarks) {
            showBookmarks = false
        } else if (showHistory) {
            showHistory = false
        } else if (showTabSwitcher) {
            showTabSwitcher = false
        } else if (activeTab?.isFullScreen == true) {
            if (activeGeckoSession != null) {
                activeGeckoSession.exitFullScreen()
            } else if (viewModel.browserCustomViewCallback != null) {
                viewModel.browserCustomViewCallback?.onCustomViewHidden()
            }
        } else if (activeTab?.url == "home") {
            if (activeGeckoSession != null && activeTab.canGoBack) {
                activeGeckoSession.goBack()
            } else if (activeWebView != null && activeWebView.canGoBack()) {
                activeWebView.goBack()
            } else {
                onExit()
            }
        } else if (activeGeckoSession != null && activeTab?.canGoBack == true) {
            activeGeckoSession.goBack()
        } else if (activeWebView != null && activeWebView.canGoBack()) {
            activeWebView.goBack()
        } else {
            loadUrl("home")
        }
    }

    androidx.activity.compose.BackHandler {
        goBackOrExit()
    }

    pendingDownload?.let { download ->
        val guessedFilename = android.webkit.URLUtil.guessFileName(download.url, download.contentDisposition, download.mimeType) ?: "file"
        val sizeText = if (download.contentLength > 0) viewModel.formatFileSize(download.contentLength) else "Unknown Size"

        AlertDialog(
            onDismissRequest = { pendingDownload = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = AccentColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Confirm Download", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Do you want to download this file directly to your secure Vault?",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("File details:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightBg, shape = RoundedCornerShape(8.dp))
                            .border(1.dp, BorderColor, shape = RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Name: ", fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 13.sp)
                            Text(guessedFilename, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Size: ", fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 13.sp)
                            Text(sizeText, color = TextPrimary, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Type: ", fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 13.sp)
                            Text(download.mimeType, color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.startVaultDownload(
                            context,
                            download.url,
                            download.userAgent,
                            download.contentDisposition,
                            download.mimeType,
                            download.contentLength
                        )
                        pendingDownload = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
                ) {
                    Text("Download", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDownload = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = LightCard,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }

    if (showDownloads) {
        DownloadsScreen(
            viewModel = viewModel,
            onBack = { showDownloads = false },
            context = context
        )
        return
    }

    if (showBookmarks) {
        SecretBrowserBookmarksScreen(
            browserBookmarks = browserBookmarks,
            onBack = { showBookmarks = false },
            onSelectBookmark = { url ->
                showBookmarks = false
                loadUrl(url)
            },
            onDeleteBookmark = { url ->
                viewModel.removeBrowserBookmark(url)
            }
        )
        return
    }

    if (showHistory) {
        SecretBrowserHistoryScreen(
            browserHistory = browserHistory,
            onBack = { showHistory = false },
            onSelectHistoryItem = { url ->
                showHistory = false
                loadUrl(url)
            },
            onClearHistory = {
                viewModel.clearBrowserHistory()
            },
            onDeleteHistoryItem = { item ->
                viewModel.deleteBrowserHistoryItem(item)
            }
        )
        return
    }

    if (showSettings) {
        SecretBrowserSettingsDashboard(
            tabs = tabs,
            browserBookmarks = browserBookmarks,
            browserHistory = browserHistory,
            searchEngine = searchEngine,
            savePasswords = savePasswords,
            clearHistoryOnExit = clearHistoryOnExit,
            clearTempOnExit = clearTempOnExit,
            useGeckoView = useGeckoView,
            trackingProtectionEnabled = trackingProtectionEnabled,
            onSetTrackingProtectionEnabled = { viewModel.setTrackingProtectionEnabled(it) },
            totalTrackersBlocked = totalTrackersBlocked,
            onBack = { showSettings = false },
            onShowSearchEngineDialog = { showSearchEngineDialog = true },
            onSetSavePasswords = { viewModel.setSavePasswords(it) },
            onSetClearHistoryOnExit = { viewModel.setClearHistoryOnExit(it) },
            onSetClearTempOnExit = { viewModel.setClearTempOnExit(it) },
            onClearBrowsingData = { clearHistory, clearCookies, clearCache, clearSiteData, onResult ->
                SecretBrowserPrivacyHelper.clearBrowsingData(
                    context = context,
                    clearHistory = clearHistory,
                    clearCookies = clearCookies,
                    clearCache = clearCache,
                    clearSiteData = clearSiteData,
                    viewModel = viewModel,
                    onResult = onResult
                )
            },
            onShowDownloads = {
                showSettings = false
                showDownloads = true
            }
        )
        return
    }

    if (showSearchEngineDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showSearchEngineDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
                    Text("Search Engine", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                    val engines = listOf("Google", "DuckDuckGo", "Bing", "Yahoo")
                    engines.forEach { engine ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setSearchEngine(engine)
                                    showSearchEngineDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = searchEngine == engine,
                                onClick = {
                                    viewModel.setSearchEngine(engine)
                                    showSearchEngineDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = AccentColor, unselectedColor = TextSecondary)
                            )
                            Text(engine, color = TextPrimary, fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp))
                        }
                    }
                }
            }
        }
    }

    if (showSiteSecurityDialog && currentHost != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showSiteSecurityDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (isProtectionActive) SuccessColor.copy(alpha = 0.08f) else TextSecondary.copy(alpha = 0.08f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = if (isProtectionActive) SuccessColor else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text("Site Security Info", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(currentHost, color = TextSecondary, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }

                    androidx.compose.material3.HorizontalDivider(color = BorderColor)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SuccessColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Connection is encrypted & isolated",
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isProtectionActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isProtectionActive) SuccessColor else DangerColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isProtectionActive) "Tracking Protection ACTIVE" else "Tracking Protection INACTIVE",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    val pageBlocked = activeTab?.blockedCount ?: 0
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = if (pageBlocked > 0) DangerColor else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "$pageBlocked trackers blocked on this page",
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    }

                    androidx.compose.material3.HorizontalDivider(color = BorderColor)

                    Text(
                        text = "PER-SITE OVERRIDE",
                        color = AccentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (siteOverride == true) SuccessColor.copy(alpha = 0.1f) else Color.Transparent)
                                .border(1.dp, if (siteOverride == true) SuccessColor else BorderColor, RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.setSiteTrackingProtection(currentHost, true)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = siteOverride == true,
                                onClick = { viewModel.setSiteTrackingProtection(currentHost, true) },
                                colors = RadioButtonDefaults.colors(selectedColor = SuccessColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Always Enable Protection", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (siteOverride == false) DangerColor.copy(alpha = 0.1f) else Color.Transparent)
                                .border(1.dp, if (siteOverride == false) DangerColor else BorderColor, RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.setSiteTrackingProtection(currentHost, false)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = siteOverride == false,
                                onClick = { viewModel.setSiteTrackingProtection(currentHost, false) },
                                colors = RadioButtonDefaults.colors(selectedColor = DangerColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Always Disable Protection", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (siteOverride == null) AccentColor.copy(alpha = 0.1f) else Color.Transparent)
                                .border(1.dp, if (siteOverride == null) AccentColor else BorderColor, RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.setSiteTrackingProtection(currentHost, null)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = siteOverride == null,
                                onClick = { viewModel.setSiteTrackingProtection(currentHost, null) },
                                colors = RadioButtonDefaults.colors(selectedColor = AccentColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Use Global Default (${if (trackingProtectionEnabled) "ON" else "OFF"})", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Button(
                        onClick = { showSiteSecurityDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize().background(LightBg)) {
        Column(modifier = Modifier.fillMaxSize().let { if (activeTab?.isFullScreen == true) it else it.statusBarsPadding() }) {

            // TOP ADDRESS / BAR AREA
            if (activeTab?.isFullScreen != true) Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightCard)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .drawBehind {
                        drawLine(
                            color = BorderColor,
                            start = androidx.compose.ui.geometry.Offset(0f, size.height),
                            end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isEditingUrl) {
                    IconButton(
                        onClick = { isEditingUrl = false },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Close, "Cancel", tint = TextPrimary, modifier = Modifier.size(20.dp))
                    }

                    OutlinedTextField(
                        value = editingUrlText,
                        onValueChange = { editingUrlText = it },
                        placeholder = { Text("Search or enter URL", color = TextSecondary, fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LightBg,
                            unfocusedContainerColor = LightBg,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = BorderColor,
                            cursorColor = AccentColor,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                val target = editingUrlText.trim()
                                if (target.isNotEmpty()) {
                                    loadUrl(target)
                                }
                                isEditingUrl = false
                            }
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                    )

                    IconButton(
                        onClick = {
                            val target = editingUrlText.trim()
                            if (target.isNotEmpty()) {
                                loadUrl(target)
                            }
                            isEditingUrl = false
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Check, "Go", tint = AccentColor, modifier = Modifier.size(20.dp))
                    }
                } else {
                    IconButton(
                        onClick = { onExit() },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary, modifier = Modifier.size(20.dp))
                    }

                    if (isHome) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                                .clickable {
                                    editingUrlText = ""
                                    isEditingUrl = true
                                }
                        ) {
                            Icon(Icons.Default.Security, "Secret Browser", tint = AccentColor, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Secret Browser",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .height(38.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(LightBg)
                                .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                                .clickable {
                                    editingUrlText = activeTab?.url ?: ""
                                    isEditingUrl = true
                                }
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = if (isProtectionActive) Icons.Default.Security else Icons.Default.Lock,
                                    contentDescription = "Security Info",
                                    tint = if (isProtectionActive) SuccessColor else TextSecondary,
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable {
                                            showSiteSecurityDialog = true
                                        }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val displayUrl = try {
                                    val urlStr = activeTab?.url ?: ""
                                    if (urlStr == "home" || urlStr.isEmpty()) {
                                        "Secret Browser"
                                    } else {
                                        val uri = android.net.Uri.parse(urlStr)
                                        val host = uri.host
                                        if (!host.isNullOrEmpty()) {
                                            host.removePrefix("www.")
                                        } else {
                                            urlStr
                                        }
                                    }
                                } catch(e: Exception) {
                                    activeTab?.title ?: "Website"
                                }
                                Text(
                                    text = displayUrl,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (activeTab?.isLoading == true) {
                                        stopLoading()
                                    } else {
                                        reload()
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (activeTab?.isLoading == true) Icons.Default.Close else Icons.Default.Refresh,
                                    contentDescription = if (activeTab?.isLoading == true) "Stop" else "Refresh",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.MoreVert, "More Options", tint = TextPrimary, modifier = Modifier.size(20.dp))
                    }
                }
            }

            if (showFindInPage && !isHome) {
                FindInPageBar(
                    query = findInPageText,
                    onQueryChange = { text ->
                        findInPageText = text
                        performFindInPage(text, true)
                    },
                    currentMatch = findInPageMatchCurrent,
                    totalMatch = findInPageMatchTotal,
                    onPrev = { findPreviousMatch() },
                    onNext = { findNextMatch() },
                    onClose = { closeFindInPage() }
                )
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (isHome) {
                    SecretBrowserHome(
                        tabs = tabs,
                        activeTabId = activeTabId ?: "",
                        searchEngine = searchEngine,
                        browserBookmarks = browserBookmarks,
                        browserHistory = browserHistory,
                        onSearch = { query ->
                            var target = query.trim()
                            if (!target.startsWith("http://") && !target.startsWith("https://")) {
                                if (target.contains(".") && !target.contains(" ")) {
                                    target = "https://$target"
                                } else {
                                    val q = java.net.URLEncoder.encode(target, "UTF-8")
                                    target = when (searchEngine) {
                                        "DuckDuckGo" -> "https://duckduckgo.com/?q=$q&kl=us-en"
                                        "Bing" -> "https://www.bing.com/search?q=$q&setlang=en&cc=US"
                                        "Yahoo" -> "https://search.yahoo.com/search?p=$q&ei=UTF-8&vc=US&vl=en"
                                        else -> "https://www.google.com/search?q=$q&hl=en&gl=US"
                                    }
                                }
                            }
                            loadUrl(target)
                        },
                        onOpenNewTab = { openNewTab(it) },
                        onSelectActiveTab = { activeTabId = it },
                        onCloseTab = closeTab,
                        onShowBookmarks = { showBookmarks = true },
                        onShowHistory = { showHistory = true },
                        onShowDownloads = { showDownloads = true },
                        onShowSettings = { showSettings = true },
                        onShowSearchEngineDialog = { showSearchEngineDialog = true },
                        onClearAllData = {
                            if (clearHistoryOnExit) {
                                viewModel.clearBrowserHistory()
                            }
                            clearAllBrowsingData(context, tabs, webViews)
                            geckoSessions.clear()
                            openNewTab("home")
                            Toast.makeText(context, "Session Purged Successfully!", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    if (activeGeckoSession != null) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            key(activeGeckoSession.hashCode()) {
                                AndroidView(
                                    factory = { ctx ->
                                        org.mozilla.geckoview.GeckoView(ctx).apply {
                                            setSession(activeGeckoSession)
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            val progressAlpha by animateFloatAsState(
                                targetValue = if (activeTab?.isLoading == true) 1f else 0f,
                                animationSpec = tween(durationMillis = 300),
                                label = "GeckoProgressAlpha"
                            )
                            if (progressAlpha > 0f) {
                                LinearProgressIndicator(
                                    progress = { (activeTab?.progress ?: 0) / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .align(Alignment.TopCenter)
                                        .graphicsLayer { alpha = progressAlpha },
                                    color = AccentColor,
                                    trackColor = Color.Transparent
                                )
                            }
                        }
                    } else if (activeWebView != null) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            key(activeWebView.hashCode()) {
                                AndroidView(
                                    factory = { _ ->
                                        (activeWebView.parent as? android.view.ViewGroup)?.removeView(activeWebView)
                                        activeWebView
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            val progressAlpha by animateFloatAsState(
                                targetValue = if (activeTab?.isLoading == true) 1f else 0f,
                                animationSpec = tween(durationMillis = 300),
                                label = "WebProgressAlpha"
                            )
                            if (progressAlpha > 0f) {
                                LinearProgressIndicator(
                                    progress = { (activeTab?.progress ?: 0) / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .align(Alignment.TopCenter)
                                        .graphicsLayer { alpha = progressAlpha },
                                    color = AccentColor,
                                    trackColor = Color.Transparent
                                )
                            }
                        }
                    }

                    val currentUrl = activeTab?.url ?: ""
                    val canBookmark = currentUrl.isNotEmpty() && currentUrl != "home" && currentUrl != "about:blank" &&
                            !currentUrl.startsWith("data:") && !currentUrl.startsWith("file:") && !currentUrl.startsWith("about:")

                    if (canBookmark) {
                        val isBookmarked = browserBookmarks.any { it.url == currentUrl }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(horizontal = 16.dp, vertical = 90.dp)
                                .size(48.dp)
                                .background(LightCard, CircleShape)
                                .border(1.dp, BorderColor, CircleShape)
                                .premiumPressClick {
                                    if (isBookmarked) {
                                        viewModel.removeBrowserBookmark(currentUrl)
                                        Toast.makeText(context, "Removed from Bookmarks", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.addBrowserBookmark(activeTab?.title ?: "New Tab", currentUrl)
                                        Toast.makeText(context, "Added to Bookmarks", Toast.LENGTH_SHORT).show()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isBookmarked) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) Color(0xFFFFC107) else TextSecondary
                            )
                        }
                    }
                }
            }

            // BOTTOM DOCK NAVIGATION REBUILD
            if (activeTab?.isFullScreen != true) Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { goBack() },
                            enabled = activeTab?.canGoBack == true,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = if (activeTab?.canGoBack == true) TextPrimary else TextSecondary.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { goForward() },
                            enabled = activeTab?.canGoForward == true,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Forward",
                                tint = if (activeTab?.canGoForward == true) TextPrimary else TextSecondary.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                stopLoading()
                                loadUrl("home")
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { openNewTab("home") },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "New Tab",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.5.dp, TextPrimary, RoundedCornerShape(6.dp))
                                .premiumPressClick { showTabSwitcher = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tabs.size.toString(),
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // TAB SWITCHER DIALOG OVERLAY (Light mode)
        if (showTabSwitcher) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showTabSwitcher = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(16.dp)
                        .clickable(enabled = false) {},
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    border = BorderStroke(1.dp, BorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Active Sessions (${tabs.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            IconButton(onClick = { openNewTab("home"); showTabSwitcher = false }) {
                                Icon(Icons.Default.Add, "New Tab", tint = AccentColor)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 280.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(tabs) { tab ->
                                val isActive = tab.id == activeTabId
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            activeTabId = tab.id
                                            showTabSwitcher = false
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isActive) AccentColor.copy(alpha = 0.08f) else LightBg
                                    ),
                                    border = BorderStroke(1.dp, if (isActive) AccentColor else BorderColor)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (tab.url == "home") "Home Page" else tab.title,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (tab.url != "home") {
                                                Text(
                                                    text = tab.url,
                                                    fontSize = 12.sp,
                                                    color = TextSecondary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        IconButton(
                                            onClick = {
                                                closeTab(tab.id)
                                            },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Close Tab",
                                                tint = DangerColor,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // BROWSER MENU OVERLAY (Command Center style)
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showMenu = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .clickable(enabled = false) {},
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBg),
                    border = BorderStroke(1.dp, BorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .background(TextSecondary.copy(alpha = 0.3f), CircleShape)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "COMMAND CENTER",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = LightCard),
                            border = BorderStroke(1.dp, BorderColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column {
                                val labelMode = if (activeTab?.isDesktopMode == true) "Switch to Mobile View" else "Request Desktop Site"
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .premiumPressClick {
                                            showMenu = false
                                            if (activeGeckoSession != null && activeTab != null) {
                                                val newMode = !activeTab.isDesktopMode
                                                val index = tabs.indexOfFirst { it.id == activeTabId }
                                                if (index != -1) {
                                                    tabs[index] = tabs[index].copy(isDesktopMode = newMode)
                                                }
                                                GeckoSessionManager.removeAndDestroySession(activeTab.id)
                                                val newSession = createPrivateGeckoSession(
                                                    ctx = context,
                                                    tabId = activeTab.id,
                                                    initialUrl = activeTab.url,
                                                    isDesktopMode = newMode,
                                                    onDownloadRequested = { downloadUrl, userAgent, contentDisposition, mimeType, contentLength ->
                                                        pendingDownload = PendingDownloadData(downloadUrl, userAgent, contentDisposition, mimeType, contentLength)
                                                    },
                                                    onCrash = {
                                                        geckoSessions.remove(activeTab.id)
                                                    }
                                                ) { transform ->
                                                    val idx = tabs.indexOfFirst { it.id == activeTab.id }
                                                    if (idx != -1) {
                                                        tabs[idx] = transform(tabs[idx])
                                                    }
                                                }
                                                geckoSessions[activeTab.id] = newSession
                                            } else {
                                                val webView = webViews[activeTabId]
                                                if (webView != null && activeTab != null) {
                                                    val newMode = !activeTab.isDesktopMode
                                                    val index = tabs.indexOfFirst { it.id == activeTabId }
                                                    if (index != -1) {
                                                        tabs[index] = tabs[index].copy(isDesktopMode = newMode)
                                                    }
                                                    if (newMode) {
                                                        webView.settings.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                                                        webView.settings.loadWithOverviewMode = true
                                                        webView.settings.useWideViewPort = true
                                                        webView.settings.setSupportZoom(true)
                                                        webView.settings.builtInZoomControls = true
                                                        webView.settings.displayZoomControls = false
                                                    } else {
                                                        webView.settings.userAgentString = android.webkit.WebSettings.getDefaultUserAgent(context)
                                                        webView.settings.loadWithOverviewMode = true
                                                        webView.settings.useWideViewPort = true
                                                        webView.settings.setSupportZoom(true)
                                                        webView.settings.builtInZoomControls = true
                                                        webView.settings.displayZoomControls = false
                                                    }
                                                    webView.clearCache(true)
                                                    webView.reload()
                                                }
                                            }
                                        }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Laptop, null, tint = AccentColor, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(labelMode, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                androidx.compose.material3.HorizontalDivider(color = BorderColor)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .premiumPressClick { showMenu = false; showBookmarks = true }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, null, tint = AccentColor, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text("View Saved Bookmarks", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                androidx.compose.material3.HorizontalDivider(color = BorderColor)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .premiumPressClick { showMenu = false; showHistory = true }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.History, null, tint = AccentColor, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text("View Browsing Logs", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                androidx.compose.material3.HorizontalDivider(color = BorderColor)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .premiumPressClick {
                                            showMenu = false
                                            if (isHome) {
                                                Toast.makeText(context, "Cannot search on Home page", Toast.LENGTH_SHORT).show()
                                            } else {
                                                showFindInPage = true
                                                findInPageText = ""
                                            }
                                        }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Search, null, tint = AccentColor, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text("Find in Page", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                androidx.compose.material3.HorizontalDivider(color = BorderColor)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .premiumPressClick { showMenu = false; showSettings = true }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Settings, null, tint = SuccessColor, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text("Secure Engine Dashboard", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                showMenu = false
                                try {
                                    SecretBrowserSecureDelete.cleanTemporaryUploadsDirectory(context, secure = true)
                                    SecretBrowserSecureDelete.cleanStaleTemporaryRemnants(context, secure = true)
                                } catch (e: Exception) {
                                    android.util.Log.e("SecureDelete", "Panic cleanup failed", e)
                                }
                                if (clearHistoryOnExit) {
                                    viewModel.clearBrowserHistory()
                                }
                                clearAllBrowsingData(context, tabs, webViews)
                                geckoSessions.clear()
                                onPanic()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DangerColor),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Warning, "Panic Mode", tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("ACTIVATE PANIC MODE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        BrowserUploadSourceDialog(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindInPageBar(
    query: String,
    onQueryChange: (String) -> Unit,
    currentMatch: Int,
    totalMatch: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightBg)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .drawBehind {
                drawLine(
                    color = BorderColor,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Find in page...", color = TextSecondary, fontSize = 13.sp) },
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LightCard,
                unfocusedContainerColor = LightCard,
                focusedBorderColor = AccentColor,
                unfocusedBorderColor = BorderColor,
                cursorColor = AccentColor,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                }
            ),
            textStyle = TextStyle(fontSize = 13.sp),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Text(
                        text = if (totalMatch > 0) "$currentMatch/$totalMatch" else "0/0",
                        color = if (totalMatch > 0) AccentColor else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onPrev,
            modifier = Modifier.size(36.dp),
            enabled = query.isNotEmpty()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Previous Match",
                tint = if (query.isNotEmpty()) TextPrimary else TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = onNext,
            modifier = Modifier.size(36.dp),
            enabled = query.isNotEmpty()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Next Match",
                tint = if (query.isNotEmpty()) TextPrimary else TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = onClose,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
