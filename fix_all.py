import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# 1. Hoist ThemePurple for Canvas
target_canvas = """                                    // Concentric circles
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            color = ThemePurple.copy(alpha = 0.2f),"""
replacement_canvas = """                                    // Concentric circles
                                    val localThemePurple = ThemePurple
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.2f),"""
content = content.replace(target_canvas, replacement_canvas)
content = content.replace("color = ThemePurple.copy(alpha = 0.4f)", "color = localThemePurple.copy(alpha = 0.4f)")
content = content.replace("color = ThemePurple,", "color = localThemePurple,")

# 2. Fix Icons
content = content.replace("Icons.Default.Menu", "androidx.compose.material.icons.filled.Menu")
content = content.replace("Icons.Default.Person", "androidx.compose.material.icons.filled.Person")
content = content.replace("Icons.AutoMirrored.Filled.ArrowBack", "androidx.compose.material.icons.automirrored.filled.ArrowBack")
content = content.replace("Icons.AutoMirrored.Filled.List", "androidx.compose.material.icons.automirrored.filled.List")

# 3. Fix VaultFolderCard definition and calls
# I will completely rewrite the definition just to be 100% sure.
old_card = """@Composable
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
}"""

new_card = """@Composable
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

if old_card in content:
    content = content.replace(old_card, new_card)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

