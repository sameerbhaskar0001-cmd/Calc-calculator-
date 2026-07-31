import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target1 = """    var showDrawerMenu by remember { mutableStateOf(false) }"""
replacement1 = """    val drawerState = androidx.compose.material3.rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()"""

target2 = """        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF090D1A)) // Force dark background for Secure Vault
        ) {"""

replacement2 = """        androidx.compose.material3.ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                androidx.compose.material3.ModalDrawerSheet(
                    drawerContainerColor = Color(0xFF1B2031),
                    modifier = Modifier.width(300.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 48.dp, bottom = 16.dp, start = 12.dp, end = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Icon(Icons.Default.Security, contentDescription = "Vault", tint = ThemePurple, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Secure Vault", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(bottom = 8.dp))
                        
                        val menuItems = listOf(
                            "Home" to "🏠 Home Dashboard",
                            "Photos" to "🖼️ Photos Vault",
                            "Videos" to "🎥 Videos Vault",
                            "Music & Audio" to "🎵 Music & Audio",
                            "Documents" to "📄 Documents",
                            "Notes" to "📝 Secure Notes",
                            "Private Browser" to "🌐 Private Browser",
                            "Password_Generator" to "🔑 Password Generator",
                            "Secure_Voice_Note" to "🎙️ Secure Voice Note",
                            "More" to "⚙️ Vault Settings"
                        )
                        
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(menuItems) { item ->
                                val isSelected = activeSection == item.first
                                androidx.compose.material3.NavigationDrawerItem(
                                    label = { Text(item.second, color = if (isSelected) ThemePurple else Color.White, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    selected = isSelected,
                                    onClick = { 
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = item.first
                                        scope.launch { drawerState.close() }
                                    },
                                    colors = androidx.compose.material3.NavigationDrawerItemDefaults.colors(
                                        unselectedContainerColor = Color.Transparent,
                                        selectedContainerColor = ThemePurple.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                        
                        androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 8.dp))
                        androidx.compose.material3.NavigationDrawerItem(
                            label = { Text("🔒 Lock Vault Now", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Bold) },
                            selected = false,
                            onClick = { 
                                viewModel.triggerKeypressEffects(context)
                                viewModel.lockVault()
                                scope.launch { drawerState.close() }
                            },
                            colors = androidx.compose.material3.NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
                        )
                    }
                }
            },
            scrimColor = Color.Black.copy(alpha = 0.6f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF090D1A)) // Force dark background for Secure Vault
            ) {"""

target3 = """                        Box {
                            IconButton(
                                onClick = { 
                                    viewModel.triggerKeypressEffects(context)
                                    showDrawerMenu = true
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showDrawerMenu,
                                onDismissRequest = { showDrawerMenu = false },
                                modifier = Modifier
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                                    .padding(vertical = 4.dp)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("🏠 Home Dashboard", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Home"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🖼️ Photos Vault", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Photos"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🎥 Videos Vault", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Videos"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🎵 Music & Audio", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Music & Audio"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("📄 Documents", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Documents"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("📝 Secure Notes", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Notes"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🌐 Private Browser", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Private Browser"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🔑 Password Generator", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Password_Generator"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🎙️ Secure Voice Note", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Secure_Voice_Note"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("⚙️ Vault Settings", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "More"
                                        showDrawerMenu = false
                                    }
                                )
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF334155)))
                                DropdownMenuItem(
                                    text = { Text("🔒 Lock Vault Now", color = Color(0xFFEF4444), style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.lockVault()
                                        showDrawerMenu = false
                                    }
                                )
                            }
                        }"""

replacement3 = """                        IconButton(
                            onClick = { 
                                viewModel.triggerKeypressEffects(context)
                                scope.launch { drawerState.open() }
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }"""
                        
target_end = """                } // End Column
            } // End else
        } // End Main Box
    }
} // End VaultTabUnlockedContent"""

replacement_end = """                } // End Column
            } // End else
        } // End Main Box
        } // End ModalNavigationDrawer
    }
} // End VaultTabUnlockedContent"""

if target1 in content and target2 in content and target3 in content and target_end in content:
    content = content.replace(target1, replacement1)
    content = content.replace(target2, replacement2)
    content = content.replace(target3, replacement3)
    content = content.replace(target_end, replacement_end)
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(content)
    print("Replaced Drawer successfully")
else:
    print("Target not found")
    if target1 not in content: print("target1 not found")
    if target2 not in content: print("target2 not found")
    if target3 not in content: print("target3 not found")
    if target_end not in content: print("target_end not found")
