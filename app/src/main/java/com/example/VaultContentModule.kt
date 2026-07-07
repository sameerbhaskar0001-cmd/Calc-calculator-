package com.example

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.LocalAppThemeColors
import java.io.File

data class VaultItemData(
    val id: String,
    val timestamp: Long,
    val title: String,
    val isFav: Boolean,
    val folder: String,
    val type: String, // "image", "video", "document", "audio", "note"
    val path: String = "",
    val rawString: String // needed for favorite logic
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun VaultContentScreen(
    viewModel: CalculatorViewModel,
    context: Context,
    title: String,
    emptyIcon: ImageVector,
    emptyTitle: String,
    emptySubtitle: String,
    addLabel: String,
    items: List<VaultItemData>,
    folders: List<String>,
    lockedFolders: Set<String>,
    tempUnlockedFolders: Set<String>,
    onNavigateBack: () -> Unit,
    onAddClick: () -> Unit,
    onItemClick: (VaultItemData, index: Int, allFiltered: List<VaultItemData>) -> Unit,
    onCreateFolder: (String) -> Unit,
    onDeleteItems: (Set<String>) -> Unit, // passes rawStrings
    onMoveItems: (Set<String>, String) -> Unit, // passes rawStrings, new folder
    onToggleFavorite: (String) -> Unit // passes rawString
) {
    val themeColors = LocalAppThemeColors.current
    val ThemePurple = themeColors.themePurple
    val BrandBg = themeColors.brandBg
    val KeypadBg = themeColors.keypadBg

    var selectedFolder by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf("Newest") }
    var showSortOptions by remember { mutableStateOf(false) }

    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedItemRaws by remember { mutableStateOf(setOf<String>()) }

    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }

    val filteredItems = items.filter { item ->
        val matchesFolder = when (selectedFolder) {
            "All" -> true
            "Favorites" -> item.isFav
            "Uncategorized" -> item.folder.isEmpty()
            else -> item.folder == selectedFolder
        }
        val matchesSearch = if (searchQuery.isEmpty()) true else item.title.contains(searchQuery, ignoreCase = true)
        matchesFolder && matchesSearch
    }.let { list ->
        when (sortOrder) {
            "Newest" -> list.sortedByDescending { it.timestamp }
            "Oldest" -> list.sortedBy { it.timestamp }
            "Name" -> list.sortedBy { it.title.lowercase() }
            else -> list
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
        if (isSelectionMode) {
            // Selection Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(
                        onClick = {
                            isSelectionMode = false
                            selectedItemRaws = emptySet()
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = "${selectedItemRaws.size} Selected",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = {
                        if (selectedItemRaws.size == filteredItems.size) {
                            selectedItemRaws = emptySet()
                        } else {
                            selectedItemRaws = filteredItems.map { it.rawString }.toSet()
                        }
                    },
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.SelectAll, contentDescription = "Select All", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        } else {
            // Normal Top Bar (replaces the global one)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF161B2B).copy(alpha = 0.8f))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                
                // Sort Button
                Box {
                    IconButton(
                        onClick = { showSortOptions = true },
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.05f))
                    ) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    DropdownMenu(
                        expanded = showSortOptions,
                        onDismissRequest = { showSortOptions = false },
                        modifier = Modifier.background(KeypadBg)
                    ) {
                        listOf("Newest", "Oldest", "Name").forEach { order ->
                            DropdownMenuItem(
                                text = { Text(order, color = if (sortOrder == order) ThemePurple else Color.White, fontWeight = if (sortOrder == order) FontWeight.Bold else FontWeight.Normal) },
                                onClick = {
                                    sortOrder = order
                                    showSortOptions = false
                                }
                            )
                        }
                    }
                }
            }

            // Search Bar (Dashboard style)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                placeholder = { Text("Search $title...", color = Color.White.copy(alpha=0.3f), fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White.copy(alpha=0.3f), modifier = Modifier.size(20.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White.copy(alpha=0.5f), modifier = Modifier.size(16.dp))
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ThemePurple.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha=0.05f),
                    focusedContainerColor = Color(0xFF161B2B).copy(alpha = 0.5f),
                    unfocusedContainerColor = Color(0xFF161B2B).copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = ThemePurple
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }

        // Folders row
        if (!isSelectionMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VaultCategoryChip(selected = selectedFolder == "All", label = "All", onClick = { selectedFolder = "All" })
                VaultCategoryChip(selected = selectedFolder == "Favorites", label = "Favorites", icon = Icons.Default.Star, onClick = { selectedFolder = "Favorites" })
                VaultCategoryChip(selected = selectedFolder == "Uncategorized", label = "Uncategorized", onClick = { selectedFolder = "Uncategorized" })
                
                folders.forEach { folderName ->
                    val isLocked = lockedFolders.contains(folderName)
                    VaultCategoryChip(
                        selected = selectedFolder == folderName,
                        label = folderName,
                        isLocked = isLocked,
                        onClick = {
                            if (isLocked && !tempUnlockedFolders.contains(folderName)) {
                                // Handled via global pending action if implemented, or ignore
                            } else {
                                selectedFolder = folderName 
                            }
                        }
                    )
                }
                
                IconButton(
                    onClick = { showCreateFolderDialog = true },
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(ThemePurple.copy(alpha=0.1f))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Album", tint = ThemePurple, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Content Area
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (filteredItems.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(ThemePurple.copy(alpha = 0.05f))
                            .border(1.dp, ThemePurple.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(emptyIcon, contentDescription = null, modifier = Modifier.size(40.dp), tint = ThemePurple.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = if (searchQuery.isNotEmpty()) "No results found" else emptyTitle, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) "Try adjusting your search terms." else emptySubtitle,
                        color = Color.White.copy(alpha=0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 120.dp) // Space for action button
                ) {
                    items(filteredItems.size) { index ->
                        val item = filteredItems[index]
                        val isSelected = selectedItemRaws.contains(item.rawString)
                        
                        VaultItemCard(
                            item = item,
                            isSelected = isSelected,
                            isSelectionMode = isSelectionMode,
                            onClick = {
                                if (isSelectionMode) {
                                    selectedItemRaws = if (isSelected) selectedItemRaws - item.rawString else selectedItemRaws + item.rawString
                                    if (selectedItemRaws.isEmpty()) isSelectionMode = false
                                } else {
                                    onItemClick(item, index, filteredItems)
                                }
                            },
                            onLongClick = {
                                if (!isSelectionMode) {
                                    isSelectionMode = true
                                    selectedItemRaws = setOf(item.rawString)
                                }
                            }
                        )
                    }
                }
            }
            
            // Floating Action Button for Adding Items (Premium style)
            if (!isSelectionMode) {
                Button(
                    onClick = onAddClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp, pressedElevation = 4.dp)
                ) {
                    val contentColor = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White
                    Icon(Icons.Default.Add, contentDescription = addLabel, tint = contentColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(addLabel, color = contentColor, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
        }

        // Selection Bottom Bar
        AnimatedVisibility(
            visible = isSelectionMode,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Surface(
                color = Color(0xFF161B2B).copy(alpha = 0.95f),
                modifier = Modifier.fillMaxWidth().height(80.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                shadowElevation = 16.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val hasFavs = filteredItems.filter { selectedItemRaws.contains(it.rawString) }.any { it.isFav }
                    SelectionActionButton(
                        icon = if (hasFavs) Icons.Default.StarOutline else Icons.Default.Star,
                        label = if (hasFavs) "Unfavorite" else "Favorite",
                        color = Color.White,
                        onClick = {
                            selectedItemRaws.forEach { onToggleFavorite(it) }
                            isSelectionMode = false
                            selectedItemRaws = emptySet()
                        }
                    )
                    SelectionActionButton(
                        icon = Icons.Default.DriveFileMove,
                        label = "Move",
                        color = Color.White,
                        onClick = { showMoveDialog = true }
                    )
                    SelectionActionButton(
                        icon = Icons.Default.Delete,
                        label = "Delete",
                        color = Color(0xFFEF5350),
                        onClick = {
                            onDeleteItems(selectedItemRaws)
                            isSelectionMode = false
                            selectedItemRaws = emptySet()
                        }
                    )
                }
            }
        }
    }

    if (showCreateFolderDialog) {
        var newFolderName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateFolderDialog = false },
            containerColor = KeypadBg,
            title = { Text("Create Album", color = Color.White, fontWeight = FontWeight.Bold) },
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
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newFolderName.isNotBlank()) {
                            onCreateFolder(newFolderName.trim())
                            showCreateFolderDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Create", color = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White) }
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
            title = { Text("Move Items", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    TextButton(onClick = {
                        onMoveItems(selectedItemRaws, "")
                        showMoveDialog = false
                        isSelectionMode = false
                        selectedItemRaws = emptySet()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Uncategorized", color = Color.White)
                    }
                    folders.forEach { folder ->
                        TextButton(onClick = {
                            onMoveItems(selectedItemRaws, folder)
                            showMoveDialog = false
                            isSelectionMode = false
                            selectedItemRaws = emptySet()
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
fun SelectionActionButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }.padding(8.dp)) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun VaultCategoryChip(selected: Boolean, label: String, icon: ImageVector? = null, isLocked: Boolean = false, onClick: () -> Unit) {
    val themeColors = LocalAppThemeColors.current
    val themePurple = themeColors.themePurple
    val brandBg = themeColors.brandBg
    val selectedContentColor = if (themePurple == Color(0xFFFFFFFF)) brandBg else Color.White
    Surface(
        color = if (selected) themePurple else Color(0xFF161B2B).copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clickable { onClick() }.border(1.dp, if(selected) Color.Transparent else Color.White.copy(alpha=0.05f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isLocked) {
                Icon(Icons.Default.Lock, contentDescription = "Locked", modifier = Modifier.size(14.dp), tint = if (selected) selectedContentColor else Color.White.copy(alpha = 0.4f))
            } else if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = if (selected) selectedContentColor else Color.White.copy(alpha = 0.4f))
            }
            Text(
                text = label,
                color = if (selected) selectedContentColor else Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VaultItemCard(
    item: VaultItemData,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val themePurple = LocalAppThemeColors.current.themePurple
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF161B2B).copy(alpha = 0.6f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        when (item.type) {
            "image" -> {
                AsyncImage(
                    model = File(item.path),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            "video" -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.PlayCircleFilled, contentDescription = "Play", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(40.dp))
                }
            }
            "audio" -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AudioFile, contentDescription = "Audio", tint = themePurple.copy(alpha = 0.6f), modifier = Modifier.size(40.dp))
                }
            }
            "document" -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Description, contentDescription = "Document", tint = Color(0xFF29B6F6).copy(alpha = 0.6f), modifier = Modifier.size(40.dp))
                    Text(item.title.substringAfterLast('.').take(4).uppercase(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp))
                }
            }
            "note" -> {
                Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    Text(item.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
        
        // Gradient overlay for titles on media
        if (item.type in listOf("image", "video")) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    ))
                    .padding(8.dp)
            ) {
                Text(item.title, color = Color.White, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }

        if (item.isFav && !isSelectionMode) {
            Icon(
                Icons.Default.Star,
                contentDescription = "Favorite",
                tint = Color(0xFFFFD600),
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(16.dp)
            )
        }

        if (isSelectionMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isSelected) themePurple.copy(alpha=0.4f) else Color.Black.copy(alpha=0.5f))
            )
            Icon(
                if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Select",
                tint = if (isSelected) themePurple else Color.White.copy(alpha=0.8f),
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp).size(24.dp)
            )
        }
    }
}
