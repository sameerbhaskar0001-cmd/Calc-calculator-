import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF161B2B).copy(alpha = 0.95f))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(32.dp))
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Home (Grid)
                IconButton(onClick = { activeSection = "Home" }) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Home",
                        tint = if (activeSection == "Home") ThemePurple else Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                // Lock Vault
                IconButton(onClick = { 
                    viewModel.triggerKeypressEffects(context)
                    viewModel.lockVault()
                    onLockExit() 
                }) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Vault",
                        tint = ThemePurple,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                // Search
                IconButton(onClick = { showSearchDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                // Settings/More
                IconButton(onClick = { activeSection = "Settings" }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More",
                        tint = if (activeSection == "Settings") ThemePurple else Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }"""

replacement = """            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF161B2B).copy(alpha = 0.95f))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(32.dp))
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Vault (Home)
                IconButton(onClick = { activeSection = "Home" }, modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Vault",
                            tint = if (activeSection == "Home") ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // Browser
                IconButton(onClick = { activeSection = "Private Browser" }, modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Browser",
                            tint = if (activeSection == "Private Browser") ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // More
                IconButton(onClick = { activeSection = "Settings" }, modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "More",
                            tint = if (activeSection == "Settings") ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
