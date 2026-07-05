package com.example

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.LocalAppThemeColors
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MediaGalleryScreen(
    viewModel: CalculatorViewModel,
    context: Context,
    mediaType: String, // "image" or "video"
    onNavigateBack: () -> Unit,
    onViewMedia: (List<String>, Int) -> Unit
) {
    val vaultFiles by viewModel.vaultFiles.collectAsState()
    val favoriteFiles by viewModel.favoriteFiles.collectAsState()
    val fileFolders by viewModel.fileFolders.collectAsState()
    val vaultFolders by viewModel.vaultFolders.collectAsState()
    val lockedFolders by viewModel.lockedFolders.collectAsState()
    val tempUnlockedFolders by viewModel.tempUnlockedFolders.collectAsState()
    val blurThumbnails by viewModel.blurThumbnails.collectAsState()
    
    val themeColors = LocalAppThemeColors.current
    val ThemePurple = themeColors.themePurple
    val BrandBg = themeColors.brandBg
    val KeypadBg = themeColors.keypadBg

    var selectedMediaFolder by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf("Newest") }
    var showSortOptions by remember { mutableStateOf(false) }
    
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedFiles by remember { mutableStateOf(setOf<String>()) }
    
    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }

    val allMediaFiles = vaultFiles.filter {
        val parts = it.split("|||")
        parts.size >= 4 && parts[3].startsWith("$mediaType/")
    }

    val filteredFiles = allMediaFiles.filter { fileStr ->
        val parts = fileStr.split("|||")
        if (parts.size < 4) return@filter false
        val id = parts[0]
        val name = parts[2].lowercase()
        val isFav = favoriteFiles.contains(id)
        val assocFolder = fileFolders[id] ?: ""
        
        val matchesFolder = when (selectedMediaFolder) {
            "All" -> true
            "Favorites" -> isFav
            "Default" -> assocFolder.isEmpty()
            else -> assocFolder == selectedMediaFolder
        }
        
        val matchesSearch = if (searchQuery.isEmpty()) true else name.contains(searchQuery.lowercase())
        
        matchesFolder && matchesSearch
    }.let { list ->
        when (sortOrder) {
            "Newest" -> list.sortedByDescending { it.split("|||")[1].toLongOrNull() ?: 0L }
            "Oldest" -> list.sortedBy { it.split("|||")[1].toLongOrNull() ?: 0L }
            "Name" -> list.sortedBy { it.split("|||")[2].lowercase() }
            else -> list
        }
    }

    // Top Bar
    Column(modifier = Modifier.fillMaxSize().background(BrandBg)) {
        if (isSelectionMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { 
                        isSelectionMode = false
                        selectedFiles = emptySet()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close Selection", tint = Color.White)
                    }
                    Text(
                        text = "${selectedFiles.size} Selected",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    IconButton(onClick = {
                        if (selectedFiles.size == filteredFiles.size) {
                            selectedFiles = emptySet()
                        } else {
                            selectedFiles = filteredFiles.map { it.split("|||")[0] }.toSet()
                        }
                    }) {
                        Icon(Icons.Default.SelectAll, contentDescription = "Select All", tint = Color.White)
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (mediaType == "image") "Photos" else "Videos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                
                IconButton(onClick = { showSortOptions = true }) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort", tint = Color.White)
                }
                
                DropdownMenu(
                    expanded = showSortOptions,
                    onDismissRequest = { showSortOptions = false },
                    modifier = Modifier.background(KeypadBg)
                ) {
                    listOf("Newest", "Oldest", "Name").forEach { order ->
                        DropdownMenuItem(
                            text = { Text(order, color = if (sortOrder == order) ThemePurple else Color.White) },
                            onClick = {
                                sortOrder = order
                                showSortOptions = false
                            }
                        )
                    }
                }
            }
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                placeholder = { Text("Search ${if (mediaType == "image") "photos" else "videos"}...", color = Color.White.copy(alpha=0.4f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White.copy(alpha=0.4f)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White.copy(alpha=0.6f))
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ThemePurple,
                    unfocusedBorderColor = Color.White.copy(alpha=0.1f),
                    focusedContainerColor = KeypadBg,
                    unfocusedContainerColor = KeypadBg,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Folders row
        if (!isSelectionMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MediaGalleryFolderChip(
                    selected = selectedMediaFolder == "All",
                    label = "All",
                    onClick = { selectedMediaFolder = "All" }
                )
                MediaGalleryFolderChip(
                    selected = selectedMediaFolder == "Favorites",
                    label = "Favorites ⭐",
                    onClick = { selectedMediaFolder = "Favorites" }
                )
                MediaGalleryFolderChip(
                    selected = selectedMediaFolder == "Default",
                    label = "Uncategorized",
                    onClick = { selectedMediaFolder = "Default" }
                )
                vaultFolders.forEach { folderName ->
                    val isLocked = lockedFolders.contains(folderName)
                    MediaGalleryFolderChip(
                        selected = selectedMediaFolder == folderName,
                        label = folderName,
                        isLocked = isLocked,
                        onClick = { 
                            if (isLocked && !tempUnlockedFolders.contains(folderName)) {
                                // Handled via global pending action normally, but here we can just skip or trigger it
                                // For now, we will just allow selection if unlocked, or rely on parent for auth
                                // In a real flow, we'd trigger the pin dialog.
                            } else {
                                selectedMediaFolder = folderName 
                            }
                        }
                    )
                }
                TextButton(onClick = { showCreateFolderDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Folder", modifier = Modifier.size(16.dp), tint = ThemePurple)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Folder", fontSize = 12.sp, color = ThemePurple)
                }
            }
        }

        // Content
        if (filteredFiles.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        if (mediaType == "image") Icons.Default.Image else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.White.copy(alpha=0.1f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) "No results found" else "No Secure ${if (mediaType == "image") "Photos" else "Videos"}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) "Try a different search term" else "Tap '+' to import files securely.",
                        color = Color.White.copy(alpha=0.5f),
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredFiles) { fileStr ->
                    val parts = fileStr.split("|||")
                    val id = parts[0]
                    val originalName = parts[2]
                    val mimeType = parts[3]
                    val path = parts[4]
                    val isFav = favoriteFiles.contains(id)
                    val isSelected = selectedFiles.contains(id)
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(KeypadBg)
                            .combinedClickable(
                                onClick = {
                                    if (isSelectionMode) {
                                        selectedFiles = if (isSelected) {
                                            selectedFiles - id
                                        } else {
                                            selectedFiles + id
                                        }
                                        if (selectedFiles.isEmpty()) isSelectionMode = false
                                    } else {
                                        onViewMedia(filteredFiles, filteredFiles.indexOf(fileStr))
                                    }
                                },
                                onLongClick = {
                                    if (!isSelectionMode) {
                                        isSelectionMode = true
                                        selectedFiles = setOf(id)
                                    }
                                }
                            )
                    ) {
                        if (mimeType.startsWith("image/")) {
                            AsyncImage(
                                model = File(path),
                                contentDescription = originalName,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .then(if (blurThumbnails) Modifier.blur(16.dp) else Modifier),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha=0.8f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayCircleOutline, contentDescription = "Video", tint = Color.White.copy(alpha=0.8f), modifier = Modifier.size(32.dp))
                            }
                        }
                        
                        if (isFav && !isSelectionMode) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Favorite",
                                tint = Color(0xFFFFD600),
                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(16.dp)
                            )
                        }

                        if (isSelectionMode) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(if (isSelected) ThemePurple.copy(alpha=0.3f) else Color.Black.copy(alpha=0.4f))
                            )
                            Icon(
                                if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Select",
                                tint = if (isSelected) ThemePurple else Color.White.copy(alpha=0.8f),
                                modifier = Modifier.align(Alignment.TopStart).padding(6.dp).size(20.dp)
                            )
                        }
                    }
                }
            }
        }
        
        // Bottom Action Bar for Selection Mode
        AnimatedVisibility(
            visible = isSelectionMode,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Surface(
                color = KeypadBg,
                modifier = Modifier.fillMaxWidth().height(72.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val hasFavs = selectedFiles.any { favoriteFiles.contains(it) }
                    IconButton(onClick = { 
                        selectedFiles.forEach { viewModel.toggleFavoriteFile(it) }
                        isSelectionMode = false
                        selectedFiles = emptySet()
                    }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(if (hasFavs) Icons.Default.StarOutline else Icons.Default.Star, contentDescription = "Favorite", tint = Color.White)
                            Text(if (hasFavs) "Unfav" else "Fav", color = Color.White, fontSize = 10.sp)
                        }
                    }
                    IconButton(onClick = { showMoveDialog = true }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.DriveFileMove, contentDescription = "Move", tint = Color.White)
                            Text("Move", color = Color.White, fontSize = 10.sp)
                        }
                    }
                    IconButton(onClick = { 
                        // Delete logic
                        val filesToDelete = allMediaFiles.filter { selectedFiles.contains(it.split("|||")[0]) }
                        filesToDelete.forEach { viewModel.deleteVaultFile(it) }
                        isSelectionMode = false
                        selectedFiles = emptySet()
                    }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE53935))
                            Text("Delete", color = Color(0xFFE53935), fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
    
    if (showCreateFolderDialog) {
        var newFolderName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateFolderDialog = false },
            containerColor = KeypadBg,
            title = { Text("Create Album", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    label = { Text("Album Name", color = Color.White.copy(0.6f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ThemePurple,
                        unfocusedBorderColor = Color.White.copy(alpha=0.3f),
                        focusedTextColor = Color.White
                    ),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newFolderName.isNotBlank()) {
                            viewModel.addFolder(newFolderName.trim())
                            showCreateFolderDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) { Text("Create", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showCreateFolderDialog = false }) {
                    Text("Cancel", color = Color.White.copy(0.6f))
                }
            }
        )
    }
    
    if (showMoveDialog) {
        AlertDialog(
            onDismissRequest = { showMoveDialog = false },
            containerColor = KeypadBg,
            title = { Text("Move to Album", color = Color.White) },
            text = {
                Column {
                    TextButton(onClick = { 
                        selectedFiles.forEach { viewModel.setFolderForFile(it, "") }
                        showMoveDialog = false
                        isSelectionMode = false
                        selectedFiles = emptySet()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Uncategorized", color = Color.White)
                    }
                    vaultFolders.forEach { folder ->
                        TextButton(onClick = { 
                            selectedFiles.forEach { viewModel.setFolderForFile(it, folder) }
                            showMoveDialog = false
                            isSelectionMode = false
                            selectedFiles = emptySet()
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(folder, color = Color.White)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMoveDialog = false }) {
                    Text("Cancel", color = Color.White.copy(0.6f))
                }
            }
        )
    }
}

@Composable
fun MediaGalleryFolderChip(
    selected: Boolean,
    label: String,
    isLocked: Boolean = false,
    onClick: () -> Unit
) {
    val themePurple = LocalAppThemeColors.current.themePurple
    Surface(
        color = if (selected) themePurple else Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isLocked) {
                Icon(Icons.Default.Lock, contentDescription = "Locked", modifier = Modifier.size(12.dp), tint = if (selected) Color.White else Color.White.copy(alpha = 0.6f))
            }
            Text(
                text = label,
                color = if (selected) Color.White else Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
