import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

# Replace the EmptyVaultSectionState for Recently Deleted
# Finding the block
pattern_empty = r'if \(recentlyDeletedFiles\.isEmpty\(\)\) \{\s*EmptyVaultSectionState\([\s\S]*?description = "[^"]*"\s*\)\s*\} else \{'

replacement_empty = '''if (recentlyDeletedFiles.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🗑️",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Recently Deleted is empty.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Deleted items remain here for 30 days before being permanently removed.",
                                fontSize = 14.sp,
                                color = TextMedium.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    } else {'''

text = re.sub(pattern_empty, replacement_empty, text)

# Finding the Box and Icon block for thumbnails
pattern_thumb = r'Box\(\s*modifier = Modifier\s*\.size\(50\.dp\)\s*\.clip\(RoundedCornerShape\(8\.dp\)\)\s*\.background\(ThemeLightPurple\),\s*contentAlignment = Alignment\.Center\s*\)\s*\{\s*if\s*\(mimeType\.startsWith\("image/"\)\)\s*\{[\s\S]*?\}\s*\}'

replacement_thumb = '''val ctx = androidx.compose.ui.platform.LocalContext.current
                                                val imageLoader = remember(ctx) {
                                                    coil.ImageLoader.Builder(ctx)
                                                        .components { add(coil.decode.VideoFrameDecoder.Factory()) }
                                                        .build()
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(ThemeLightPurple),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (mimeType.startsWith("image/") || mimeType.startsWith("video/")) {
                                                        AsyncImage(
                                                            model = java.io.File(path),
                                                            imageLoader = imageLoader,
                                                            contentDescription = originalName,
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                            error = painterResource(id = android.R.drawable.ic_menu_gallery)
                                                        )
                                                        if (mimeType.startsWith("video/")) {
                                                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                                                                Icon(
                                                                    imageVector = Icons.Default.PlayArrow,
                                                                    contentDescription = "Video",
                                                                    tint = Color.White,
                                                                    modifier = Modifier.size(24.dp)
                                                                )
                                                            }
                                                        }
                                                    } else if (mimeType.startsWith("audio/")) {
                                                        Icon(
                                                            imageVector = Icons.Default.AudioFile,
                                                            contentDescription = "Audio",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    } else {
                                                        Icon(
                                                            imageVector = Icons.Default.Description,
                                                            contentDescription = "Document",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }
                                                }'''

text = re.sub(pattern_thumb, replacement_thumb, text)

# Countdown Text replacement
pattern_countdown = r'Text\(\s*text = "\$remainingDays days remaining before permanent deletion",\s*fontSize = 9\.sp,\s*color = Color\(0xFFFF8A80\),\s*fontWeight = FontWeight\.Bold\s*\)'

replacement_countdown = '''Text(
                                                        text = "Auto deletes in $remainingDays days",
                                                        fontSize = 11.sp,
                                                        color = Color(0xFFFF6B6B).copy(alpha = 0.8f),
                                                        fontWeight = FontWeight.Medium
                                                    )'''
                                                    
text = re.sub(pattern_countdown, replacement_countdown, text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
