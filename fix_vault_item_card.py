import re

with open("app/src/main/java/com/example/VaultContentModule.kt", "r") as f:
    content = f.read()

target = """    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(cornerRadius), ambientColor = Color.Black.copy(alpha=0.6f), spotColor = Color.Black.copy(alpha=0.6f))
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(0xFF1C2235))
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {"""

replacement = """    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isGridView) Modifier.aspectRatio(1f) else Modifier.height(100.dp))
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(cornerRadius), ambientColor = Color.Black.copy(alpha=0.6f), spotColor = Color.Black.copy(alpha=0.6f))
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(0xFF1C2235))
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        if (!isGridView) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius))) {
                    // Content placeholder for thumbnail
                    when (item.type) {
                        "image", "video" -> {
                            val ctx = androidx.compose.ui.platform.LocalContext.current
                            val imageLoader = remember(ctx) {
                                coil.ImageLoader.Builder(ctx)
                                    .components { add(coil.decode.VideoFrameDecoder.Factory()) }
                                    .build()
                            }
                            AsyncImage(
                                model = coil.request.ImageRequest.Builder(ctx)
                                    .data(java.io.File(item.path))
                                    .crossfade(true)
                                    .build(),
                                imageLoader = imageLoader,
                                contentDescription = item.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            if (item.type == "video") {
                                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                        "audio" -> {
                            Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(listOf(themePurple.copy(alpha = 0.2f), Color.Transparent))), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AudioFile, contentDescription = "Audio", tint = themePurple.copy(alpha = 0.8f), modifier = Modifier.size(32.dp))
                            }
                        }
                        "document" -> {
                            val ext = item.title.substringAfterLast('.').lowercase()
                            val icon = when (ext) {
                                "pdf" -> Icons.Default.PictureAsPdf
                                "txt" -> Icons.Default.Description
                                else -> Icons.Default.Article
                            }
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(icon, contentDescription = "Doc", tint = Color.White, modifier = Modifier.size(32.dp))
                            }
                        }
                        "note" -> {
                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF283046)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Notes, contentDescription = "Note", tint = Color(0xFF90CAF9), modifier = Modifier.size(32.dp))
                            }
                        }
                    }
                }
                Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                    Text(item.title, color = Color.White, fontSize = 16.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.type.replaceFirstChar { it.uppercase() }, color = Color.Gray, fontSize = 12.sp)
                }
            }
        } else {"""

content = content.replace(target, replacement)

# Need to close the else block
target_end = """        if (isSelected) {
            Box(
                modifier = Modifier"""

replacement_end = """        }
        if (isSelected) {
            Box(
                modifier = Modifier"""

content = content.replace(target_end, replacement_end)

with open("app/src/main/java/com/example/VaultContentModule.kt", "w") as f:
    f.write(content)
