import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Replace ThemePurple = ThemePurple in VaultFolderCard
content = content.replace("ThemePurple = ThemePurple", "")
content = content.replace("iconTint = Color(0xFF0EA5E9), \n                                    modifier = Modifier.weight(1f),", "modifier = Modifier.weight(1f),") # Just in case it got duplicated

content = content.replace('icon = Icons.Default.Image, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.Default.Image, \n                                    iconTint = Color(0xFF0EA5E9),\n                                    modifier = Modifier.weight(1f),')
content = content.replace('icon = Icons.Default.Description, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.Default.Description, \n                                    iconTint = Color(0xFFEAB308),\n                                    modifier = Modifier.weight(1f),')
content = content.replace('icon = Icons.AutoMirrored.Filled.List, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.AutoMirrored.Filled.List, \n                                    iconTint = Color(0xFFF97316),\n                                    modifier = Modifier.weight(1f),')
content = content.replace('icon = Icons.Default.Language, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.Default.Language, \n                                    iconTint = Color(0xFF8B5CF6),\n                                    modifier = Modifier.weight(1f),')
content = content.replace('icon = Icons.Default.Movie, \n                                    modifier = Modifier.weight(1f),', 'icon = Icons.Default.Movie, \n                                    iconTint = Color(0xFF8B5CF6),\n                                    modifier = Modifier.weight(1f),')


with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

