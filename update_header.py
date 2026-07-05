import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                        IconButton(
                            onClick = { 
                                viewModel.triggerKeypressEffects(context)
                                viewModel.lockVault()
                                onLockExit()
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
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
                                    imageVector = Icons.Default.Settings,
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
                        modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 24.dp)
                    )"""

replacement = """                        IconButton(
                            onClick = { 
                                viewModel.triggerKeypressEffects(context)
                                // Handle menu click
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
                                    .background(ThemePurple.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = ThemePurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 24.dp)) {
                        Text(
                            text = "My Vault",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your space. Your privacy.",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
