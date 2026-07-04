import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# We want to replace the `// Clean and spacious Unlocked Header` up to the `// Section Contents with Crossfade Animation`
target_start = '// Clean and spacious Unlocked Header'
target_end = '// Section Contents with Crossfade Animation'

idx_start = content.find(target_start)
idx_end = content.find(target_end)

if idx_start == -1 or idx_end == -1:
    print("Could not find targets")
    exit(1)

new_header = """// Clean and spacious Unlocked Header
                if (activeSection == "Home") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { 
                                viewModel.triggerKeypressEffects(context)
                                viewModel.lockVault()
                                onLockExit()
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu / Lock",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            IconButton(
                                onClick = { 
                                    viewModel.triggerKeypressEffects(context)
                                    showSearchDialog = true
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(
                                onClick = { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Settings"
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF262D45))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "My Vault",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(
                        text = "Your space. Your privacy.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 8.dp, bottom = 24.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Home"
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1B2031))
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = viewModel.t(
                                        when(activeSection) {
                                            "Notes" -> "notes"
                                            "Photos & Videos" -> "photos_videos"
                                            "Documents" -> "documents"
                                            "Private Browser" -> "private_browser"
                                            "Explore" -> "explore"
                                            "Settings" -> "settings"
                                            else -> "secure_vault"
                                        }
                                    ),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "AES Passcode Secured",
                                    fontSize = 10.sp,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (activeSection == "Photos & Videos" || activeSection == "Documents" || activeSection == "Music & Audio") {
                                val isCurrentGrid = when (activeSection) {
                                    "Photos & Videos" -> isMediaGridView
                                    "Documents" -> isDocGridView
                                    "Music & Audio" -> isMusicGridView
                                    else -> true
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        when (activeSection) {
                                            "Photos & Videos" -> isMediaGridView = !isMediaGridView
                                            "Documents" -> isDocGridView = !isDocGridView
                                            "Music & Audio" -> isMusicGridView = !isMusicGridView
                                        }
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1B2031))
                                ) {
                                    Icon(
                                        imageVector = if (isCurrentGrid) Icons.AutoMirrored.Filled.List else Icons.Default.GridView,
                                        contentDescription = "Toggle Grid/List View",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    showSearchDialog = true
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1B2031))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Global Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Material 3 Filter Chips for Sections
                    val decoyActive by viewModel.decoyActive.collectAsState()
                    val sections = if (decoyActive) {
                        listOf("Timeline", "Notes", "Photos & Videos", "Documents", "Private Browser", "Recently Deleted", "Settings")
                    } else {
                        listOf("Timeline", "Notes", "Photos & Videos", "Documents", "Private Browser", "Explore", "Recently Deleted", "Settings")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sections.forEach { label ->
                            val isSelected = activeSection == label
                            val chipBg = if (isSelected) ThemePurple else Color(0xFF1B2031)
                            val chipText = if (isSelected) Color.White else Color(0xFF8B92A5)
                            val chipBorder = if (isSelected) Color.Transparent else Color(0xFF383F56)

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(chipBg)
                                    .border(1.dp, chipBorder, RoundedCornerShape(12.dp))
                                    .clickable {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = label
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when(label) {
                                        "Notes" -> viewModel.t("notes")
                                        "Photos & Videos" -> viewModel.t("photos_videos")
                                        "Documents" -> viewModel.t("documents")
                                        "Private Browser" -> viewModel.t("private_browser")
                                        "Explore" -> viewModel.t("explore")
                                        "Recently Deleted" -> viewModel.t("recently_deleted")
                                        "Settings" -> viewModel.t("settings")
                                        else -> label
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = chipText,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
                """

content = content[:idx_start] + new_header + content[idx_end:]

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

