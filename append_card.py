import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """@Composable
fun VaultFolderCard(title: String, count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF161B2B).copy(alpha = 0.8f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(count, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}"""

replacement = target + """

@Composable
fun EnhancedVaultCard(title: String, count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val themePurple = LocalAppThemeColors.current.themePurple
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF161B2B).copy(alpha = 0.6f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(themePurple.copy(alpha = 0.1f))
                        .border(1.dp, themePurple.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = themePurple, modifier = Modifier.size(18.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(count, color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Faux preview content based on title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F121C))
                    .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (title == "Photos") {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)))
                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)))
                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)))
                    }
                } else if (title == "Videos") {
                    Box(modifier = Modifier.fillMaxSize().padding(4.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                    }
                } else if (title == "Documents") {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                        Box(modifier = Modifier.fillMaxWidth(0.9f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                        Box(modifier = Modifier.fillMaxWidth(0.6f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                    }
                } else if (title == "Notes") {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(0.5f).height(6.dp).clip(CircleShape).background(themePurple.copy(alpha = 0.4f)))
                        Box(modifier = Modifier.fillMaxWidth(0.9f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                    }
                }
            }
        }
    }
}"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
