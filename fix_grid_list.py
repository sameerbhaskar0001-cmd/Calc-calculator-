import re

with open("app/src/main/java/com/example/VaultContentModule.kt", "r") as f:
    content = f.read()

target1 = """    var sortMenuExpanded by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("date_desc") } // date_desc, date_asc, name_asc, name_desc, size_desc"""

replacement1 = """    var sortMenuExpanded by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("date_desc") } // date_desc, date_asc, name_asc, name_desc, size_desc
    var isGridView by remember { mutableStateOf(true) }"""

content = content.replace(target1, replacement1)

target2 = """                        IconButton(onClick = { sortMenuExpanded = true }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort", tint = Color.White)
                        }"""

replacement2 = """                        IconButton(onClick = { isGridView = !isGridView }) {
                            Icon(
                                if (isGridView) Icons.AutoMirrored.Filled.List else Icons.Default.GridView, 
                                contentDescription = "Toggle View", 
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { sortMenuExpanded = true }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort", tint = Color.White)
                        }"""

content = content.replace(target2, replacement2)

target3 = """                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 110.dp),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 140.dp) // Space for action button
                    ) {"""

replacement3 = """                    LazyVerticalGrid(
                        columns = if (isGridView) GridCells.Adaptive(minSize = 110.dp) else GridCells.Fixed(1),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 140.dp) // Space for action button
                    ) {"""

content = content.replace(target3, replacement3)

target_item = """                                VaultItemCard(
                                    item = item,
                                    isSelected = isSelected,
                                    isSelectionMode = isSelectionMode,
                                    isPinned = itemIsPinned,
                                    onClick = {"""

replacement_item = """                                VaultItemCard(
                                    item = item,
                                    isSelected = isSelected,
                                    isSelectionMode = isSelectionMode,
                                    isPinned = itemIsPinned,
                                    isGridView = isGridView,
                                    onClick = {"""

content = content.replace(target_item, replacement_item)

target_card = """fun VaultItemCard(
    item: VaultItemData,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isPinned: Boolean = false
) {"""

replacement_card = """fun VaultItemCard(
    item: VaultItemData,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isPinned: Boolean = false,
    isGridView: Boolean = true
) {"""

content = content.replace(target_card, replacement_card)

with open("app/src/main/java/com/example/VaultContentModule.kt", "w") as f:
    f.write(content)
