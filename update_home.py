import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# We need to find the "Home" -> { ... } block inside VaultTabUnlockedContent
# The start is:
start_pattern = '                    "Home" -> {\n                        Column('

# Let's find the exact index of this start
idx_start = content.find(start_pattern)
if idx_start == -1:
    print("Could not find start pattern")
    exit(1)

# Now find the end of the "Home" -> { ... } block, which is right before:
#                "Notes" -> {
end_pattern = '                "Notes" -> {'
idx_end = content.find(end_pattern, idx_start)

if idx_end == -1:
    print("Could not find end pattern")
    exit(1)

# We want to replace everything from idx_start to idx_end with our new UI

new_home_ui = """                    "Home" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Status Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color(0xFF161B2B).copy(alpha = 0.8f))
                                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                                    .padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(ThemePurple.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(24.dp))
                                    }
                                    Column {
                                        Text(
                                            text = "VAULT STATUS",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.5f),
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = "Encrypted & Protected",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "All your data is safe and secure",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(20.dp))
                                }
                            }
                            
                            // FOLDERS Header
                            Text(
                                text = "FOLDERS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.5f),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                            )
                            
                            // Folders Grid
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                VaultFolderCard(
                                    title = viewModel.t("photos_videos"), 
                                    count = "${vaultFiles.count { it.mimeType.startsWith("image/") || it.mimeType.startsWith("video/") }} Items", 
                                    icon = Icons.Default.Image, 
                                    modifier = Modifier.weight(1f),
                                    ThemePurple = ThemePurple
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Photos & Videos" 
                                }
                                VaultFolderCard(
                                    title = viewModel.t("documents"), 
                                    count = "${vaultFiles.count { !it.mimeType.startsWith("image/") && !it.mimeType.startsWith("video/") && !it.mimeType.startsWith("audio/") }} Items", 
                                    icon = Icons.Default.Description, 
                                    modifier = Modifier.weight(1f),
                                    ThemePurple = ThemePurple
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Documents" 
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                VaultFolderCard(
                                    title = viewModel.t("notes"), 
                                    count = "${vaultNotes.size} Items", 
                                    icon = Icons.Default.Notes, 
                                    modifier = Modifier.weight(1f),
                                    ThemePurple = ThemePurple
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Notes" 
                                }
                                VaultFolderCard(
                                    title = viewModel.t("private_browser"), 
                                    count = "Secure", 
                                    icon = Icons.Default.Language, 
                                    modifier = Modifier.weight(1f),
                                    ThemePurple = ThemePurple
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Private Browser" 
                                }
                            }
                            
                            // RECENT ACTIVITY Header
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "RECENT ACTIVITY",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.5f),
                                    letterSpacing = 2.sp
                                )
                            }
                            
                            if (vaultFiles.isEmpty() && vaultNotes.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Your secrets are safe here.", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)
                                }
                            } else {
                                // List the 3 most recently opened items (or just default first 3 files/notes)
                                val recentFiles = vaultFiles.sortedByDescending { it.lastModified }.take(3)
                                recentFiles.forEach { file ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF1B2031))
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFF262D45)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (file.mimeType.startsWith("image/")) Icons.Default.Image else Icons.Default.InsertDriveFile,
                                                contentDescription = null,
                                                tint = Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(file.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            Text("Added recently", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }
                                }
                                
                                val recentNotes = vaultNotes.take(maxOf(0, 3 - recentFiles.size))
                                recentNotes.forEach { note ->
                                    val parts = note.split("|||")
                                    val title = parts.getOrNull(2) ?: "Note"
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF1B2031))
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFF262D45)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Notes,
                                                contentDescription = null,
                                                tint = Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            Text("Added recently", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
"""

content = content[:idx_start] + new_home_ui + content[idx_end:]

# Add VaultFolderCard composable
vault_folder_card = """
@Composable
fun VaultFolderCard(title: String, count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, ThemePurple: Color, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF161B2B).copy(alpha = 0.6f))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ThemePurple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(count, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}
"""
if "fun VaultFolderCard" not in content:
    content += vault_folder_card

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

