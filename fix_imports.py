import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

imports_to_add = """import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreHoriz
"""

# add after import androidx.compose.material.icons.filled.Search
content = content.replace("import androidx.compose.material.icons.filled.Search", "import androidx.compose.material.icons.filled.Search\n" + imports_to_add)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
