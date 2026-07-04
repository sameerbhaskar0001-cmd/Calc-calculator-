import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                            // RECENT ACTIVITY Header
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "RECENT ACTIVITY",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.5f),
                                    letterSpacing = 2.sp
                                )
                            }"""

replacement = """                            // RECENT ACTIVITY Header
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "RECENT ACTIVITY",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.5f),
                                    letterSpacing = 2.sp
                                )
                                Text(
                                    text = "View All",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ThemePurple,
                                    modifier = Modifier.clickable { activeSection = "Explore" }
                                )
                            }"""

content = content.replace(target, replacement)
content = content.replace("background(Color(0xFF1B2031))", "background(Color(0xFF161B2B).copy(alpha = 0.8f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))")

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

