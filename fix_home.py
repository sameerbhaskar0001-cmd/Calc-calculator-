import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                            // Main Vault Grid
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                VaultFolderCard(
                                    title = "Hidden Photos", 
                                    count = "12 Items", 
                                    icon = Icons.Default.Image, 
                                    iconTint = Color(0xFF0EA5E9),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Photos & Videos" 
                                }
                                VaultFolderCard(
                                    title = "Private Notes", 
                                    count = "3 Items", 
                                    icon = Icons.Default.List, 
                                    iconTint = Color(0xFFF97316),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Notes" 
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                VaultFolderCard(
                                    title = "Documents", 
                                    count = "0 Items", 
                                    icon = Icons.Default.Description, 
                                    iconTint = Color(0xFFEAB308),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Documents" 
                                }
                                VaultFolderCard(
                                    title = "Web Browser", 
                                    count = "Secure", 
                                    icon = Icons.Default.Language, 
                                    iconTint = Color(0xFF8B5CF6),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Private Browser"
                                }
                            }"""

replacement = """                            // Status Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color(0xFF161B2B).copy(alpha = 0.6f))
                                    .border(1.dp, ThemePurple.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
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
                                            .background(ThemePurple.copy(alpha = 0.1f))
                                            .border(1.dp, ThemePurple.copy(alpha = 0.2f), CircleShape),
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
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Encrypted & Protected",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ThemePurple
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(14.dp))
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "All your data is safe and secure",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                            
                            // Main Vault Grid
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                EnhancedVaultCard(
                                    title = "Photos", 
                                    count = "${vaultFiles.count { val ext = it.substringAfterLast('.', "").lowercase(); ext in listOf("jpg", "jpeg", "png") }} Items", 
                                    icon = Icons.Default.Image,
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Photos & Videos" 
                                }
                                EnhancedVaultCard(
                                    title = "Videos", 
                                    count = "${vaultFiles.count { val ext = it.substringAfterLast('.', "").lowercase(); ext in listOf("mp4", "mkv") }} Items", 
                                    icon = Icons.Default.PlayArrow,
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Photos & Videos" 
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                EnhancedVaultCard(
                                    title = "Documents", 
                                    count = "${vaultFiles.count { val ext = it.substringAfterLast('.', "").lowercase(); ext !in listOf("jpg", "jpeg", "png", "mp4", "mkv", "mp3", "wav") }} Items", 
                                    icon = Icons.Default.Description,
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Documents" 
                                }
                                EnhancedVaultCard(
                                    title = "Notes", 
                                    count = "${vaultNotes.size} Items", 
                                    icon = Icons.Default.List,
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Notes" 
                                }
                            }
                            
                            // Recent Activity
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "RECENT ACTIVITY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.5f),
                                    letterSpacing = 2.sp
                                )
                                Text(
                                    text = "View All",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ThemePurple,
                                    modifier = Modifier.clickable { activeSection = "Explore" }
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
                                val recentFiles = vaultFiles.take(3)
                                recentFiles.forEach { file ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF161B2B).copy(alpha = 0.5f))
                                            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ThemePurple.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (file.lowercase().endsWith(".jpg") || file.lowercase().endsWith(".png")) Icons.Default.Image else Icons.Default.Description,
                                                contentDescription = null,
                                                tint = ThemePurple
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(file.substringAfterLast('/'), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            Text("Added recently", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
                                        }
                                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                                    }
                                }
                            }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
