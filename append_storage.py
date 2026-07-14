import sys

with open("app/src/main/java/com/example/CalculatorScreen.kt", "a") as f:
    f.write('''

@Composable
fun StorageScreenSection(
    onBack: () -> Unit,
    onNavigateToRecentlyDeleted: () -> Unit
) {
    val themePurple = ThemePurple
    val textMedium = TextMedium
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D1A))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Storage",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Vault Storage Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF383F56).copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Vault Storage", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    }
                }
            }

            // Categories
            SettingsGroup(title = "CATEGORIES") {
                StorageCategoryRow(title = "Photos", icon = Icons.Default.Image, size = "1.2 GB", color = Color(0xFF42A5F5))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Videos", icon = Icons.Default.PlayArrow, size = "800 MB", color = Color(0xFFEF5350))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Documents", icon = Icons.Default.Description, size = "150 MB", color = Color(0xFFFFCA28))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Audio", icon = androidx.compose.material.icons.filled.MusicNote, size = "300 MB", color = Color(0xFFAB47BC))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Notes", icon = Icons.Default.Edit, size = "5 MB", color = Color(0xFF66BB6A))
            }
            
            // Recently Deleted
            SettingsGroup(title = "TRASH") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToRecentlyDeleted() }
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE53935).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Recently Deleted", tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Recently Deleted",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "45 MB", // Placeholder
                            color = textMedium,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Go",
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StorageCategoryRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, size: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = size,
            color = TextMedium,
            fontSize = 14.sp
        )
    }
}
''')

print("Done")
