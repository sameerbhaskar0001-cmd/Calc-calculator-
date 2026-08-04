import re

with open("app/src/main/java/com/example/VaultContentModule.kt", "r") as f:
    content = f.read()

target1 = """    var sortOrder by remember { mutableStateOf("Newest") }
    var showSortOptions by remember { mutableStateOf(false) }"""

replacement1 = """    var sortOrder by remember { mutableStateOf("Newest") }
    var showSortOptions by remember { mutableStateOf(false) }
    var isGridView by remember { mutableStateOf(true) }"""

content = content.replace(target1, replacement1)

target2 = """                        IconButton(onClick = { showSortOptions = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort", tint = Color.White)
                        }"""

replacement2 = """                        IconButton(onClick = { isGridView = !isGridView }) {
                            Icon(
                                if (isGridView) Icons.AutoMirrored.Filled.List else Icons.Default.GridView, 
                                contentDescription = "Toggle View", 
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { showSortOptions = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort", tint = Color.White)
                        }"""

content = content.replace(target2, replacement2)

with open("app/src/main/java/com/example/VaultContentModule.kt", "w") as f:
    f.write(content)
