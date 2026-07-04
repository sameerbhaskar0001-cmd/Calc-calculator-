import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

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

content = content.replace(old_card, new_card)

# Now we need to update the usages in "Home" -> { block.
# I'll use regex or string replace.
content = content.replace('ThemePurple = ThemePurple', '') # clear old param
content = content.replace('icon = Icons.Default.Image, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.Default.Image, \n                                    iconTint = Color(0xFF0EA5E9), \n                                    modifier = Modifier.weight(1f),')
content = content.replace('icon = Icons.Default.Description, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.Default.Description, \n                                    iconTint = Color(0xFFEAB308), \n                                    modifier = Modifier.weight(1f),')
content = content.replace('icon = Icons.Default.List, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.Default.List, \n                                    iconTint = Color(0xFFF97316), \n                                    modifier = Modifier.weight(1f),')
# Wait, Videos might not exist as a separate card! The previous had "Private Browser"
# The dashboard image has Photos, Videos, Documents, Notes
# Let's see what's in the previous row
