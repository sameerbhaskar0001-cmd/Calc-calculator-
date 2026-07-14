import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

# Edit StorageScreenSection signature
target_sig = """@Composable
fun StorageScreenSection(
    onBack: () -> Unit,
    onNavigateToRecentlyDeleted: () -> Unit
) {"""
replacement_sig = """@Composable
fun StorageScreenSection(
    viewModel: CalculatorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit,
    onNavigateToRecentlyDeleted: () -> Unit
) {
    val storageInfo by viewModel.storageInfo.collectAsState()"""
text = text.replace(target_sig, replacement_sig)

# Replace Total Vault Storage logic
target_total = """                    Text("Total Vault Storage", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Storage Bar
                    LinearProgressIndicator(
                        progress = { 0.4f }, // Placeholder
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = themePurple,
                        trackColor = Color(0xFF383F56).copy(alpha = 0.5f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("2.5 GB Used", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("10.0 GB Total", color = textMedium, fontSize = 14.sp)
                    }"""
replacement_total = """                    Text("Total Vault Storage", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val MAX_STORAGE = 15L * 1024 * 1024 * 1024 // 15 GB
                    val progress = if (MAX_STORAGE > 0) (storageInfo.totalBytes.toFloat() / MAX_STORAGE.toFloat()).coerceIn(0f, 1f) else 0f
                    // Storage Bar
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = themePurple,
                        trackColor = Color(0xFF383F56).copy(alpha = 0.5f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${storageInfo.totalUsedFormatted} Used", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("15.0 GB Total", color = textMedium, fontSize = 14.sp)
                    }"""
text = text.replace(target_total, replacement_total)

# Replace Categories
target_cats = """            SettingsGroup(title = "CATEGORIES") {
                StorageCategoryRow(title = "Photos", icon = Icons.Default.Image, size = "1.2 GB", color = Color(0xFF42A5F5))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Videos", icon = Icons.Default.PlayArrow, size = "800 MB", color = Color(0xFFEF5350))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Documents", icon = Icons.Default.Description, size = "150 MB", color = Color(0xFFFFCA28))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Audio", icon = Icons.Default.AudioFile, size = "300 MB", color = Color(0xFFAB47BC))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Notes", icon = Icons.Default.Edit, size = "5 MB", color = Color(0xFF66BB6A))
            }"""
replacement_cats = """            SettingsGroup(title = "CATEGORIES") {
                StorageCategoryRow(title = "Photos", icon = Icons.Default.Image, size = storageInfo.photosFormatted, color = Color(0xFF42A5F5))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Videos", icon = Icons.Default.PlayArrow, size = storageInfo.videosFormatted, color = Color(0xFFEF5350))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Documents", icon = Icons.Default.Description, size = storageInfo.docsFormatted, color = Color(0xFFFFCA28))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Audio", icon = Icons.Default.AudioFile, size = storageInfo.audioFormatted, color = Color(0xFFAB47BC))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Notes", icon = Icons.Default.Edit, size = storageInfo.notesFormatted, color = Color(0xFF66BB6A))
            }"""
text = text.replace(target_cats, replacement_cats)

# Replace Recently Deleted
target_trash = """                        Text(
                            text = "45 MB", // Placeholder
                            color = textMedium,"""
replacement_trash = """                        Text(
                            text = storageInfo.trashFormatted,
                            color = textMedium,"""
text = text.replace(target_trash, replacement_trash)

# Replace Home Dashboard Storage
target_home_storage = """                                        // Vault Storage Overview
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(Color(0xFF161B2B).copy(alpha = 0.5f))
                                                .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(20.dp))
                                                .clickable { activeSection = "Storage" }
                                                .padding(20.dp)
                                        ) {
                                            Column {
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.CloudQueue, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(20.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Vault Storage", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(20.dp))
                                                }
                                                Spacer(modifier = Modifier.height(16.dp))
                                                LinearProgressIndicator(
                                                    progress = { 0.25f }, // Placeholder progress
                                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                                    color = ThemePurple,
                                                    trackColor = Color(0xFF383F56).copy(alpha = 0.5f)
                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Text("2.5 GB Used", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                                    Text("7.5 GB Free", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
                                                }
                                            }
                                        }"""
replacement_home_storage = """                                        // Vault Storage Overview
                                        val storageInfo by viewModel.storageInfo.collectAsState()
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(Color(0xFF161B2B).copy(alpha = 0.5f))
                                                .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(20.dp))
                                                .clickable { activeSection = "Storage" }
                                                .padding(20.dp)
                                        ) {
                                            Column {
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.CloudQueue, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(20.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Vault Storage", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(20.dp))
                                                }
                                                Spacer(modifier = Modifier.height(16.dp))
                                                val maxStorage = 15L * 1024 * 1024 * 1024 // 15 GB
                                                val progress = if (maxStorage > 0) (storageInfo.totalBytes.toFloat() / maxStorage.toFloat()).coerceIn(0f, 1f) else 0f
                                                LinearProgressIndicator(
                                                    progress = { progress },
                                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                                    color = ThemePurple,
                                                    trackColor = Color(0xFF383F56).copy(alpha = 0.5f)
                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Text("${storageInfo.totalUsedFormatted} Used", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                                    val freeBytes = maxStorage - storageInfo.totalBytes
                                                    val freeFormatted = if (freeBytes <= 0) "0 B" else {
                                                        val units = arrayOf("B", "KB", "MB", "GB", "TB")
                                                        val digitGroups = (java.lang.Math.log10(freeBytes.toDouble()) / java.lang.Math.log10(1024.0)).toInt()
                                                        val index = if (digitGroups > 4) 4 else digitGroups
                                                        val num = freeBytes / java.lang.Math.pow(1024.0, index.toDouble())
                                                        String.format(java.util.Locale.US, "%.1f %s", num, units[index])
                                                    }
                                                    Text("$freeFormatted Free", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
                                                }
                                            }
                                        }"""
text = text.replace(target_home_storage, replacement_home_storage)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

