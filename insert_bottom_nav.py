import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                    }
                }
            }
        }

        if (showMediaAddOptions) {"""

replacement = """                    }
                }
            }
        }
        
        // Custom Bottom Navigation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
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
            }
        }

        if (showMediaAddOptions) {"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
