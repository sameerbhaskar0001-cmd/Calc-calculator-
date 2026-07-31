import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# Remove padding(16.dp) from outer Column
target_col = """            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {"""
replacement_col = """            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {"""
if target_col in content:
    content = content.replace(target_col, replacement_col)
    print("Replaced outer column")
else:
    print("Target outer column not found")

# Now we need to add padding to the top headers and Crossfade content where appropriate
# 1. Home Header
target_home_header = """                // Clean and spacious Unlocked Header
                if (activeSection == "Home") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),"""
replacement_home_header = """                // Clean and spacious Unlocked Header
                if (activeSection == "Home") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 8.dp),"""
if target_home_header in content:
    content = content.replace(target_home_header, replacement_home_header)
    print("Replaced home header")
else:
    print("Target home header not found")

# 2. Other headers
target_other_header = """                } else if (activeSection !in listOf("Photos", "Videos", "Documents", "Notes", "Music & Audio", "Password_Generator", "Secure_Voice_Note", "Metadata_Cleaner", "About")) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),"""
replacement_other_header = """                } else if (activeSection !in listOf("Photos", "Videos", "Documents", "Notes", "Music & Audio", "Password_Generator", "Secure_Voice_Note", "Metadata_Cleaner", "About")) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 8.dp),"""
if target_other_header in content:
    content = content.replace(target_other_header, replacement_other_header)
    print("Replaced other header")
else:
    print("Target other header not found")

# 3. Home Content (Crossfade)
target_home_content = """                    "Home" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {"""
replacement_home_content = """                    "Home" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {"""
if target_home_content in content:
    content = content.replace(target_home_content, replacement_home_content)
    print("Replaced home content")
else:
    print("Target home content not found")

# 4. Settings content, Profile, etc.
# Actually, everything except the full-screen apps (like Notes, Metadata Cleaner, etc) probably needs padding.
# Wait, Profile, Settings, Backup, App Disguise, Security, Authentication, etc all render in the Crossfade.
# It's better to just let them have padding by adding it inside the Crossfade for specific sections, or we can just apply padding to them individually.
# Let's write the modified content back.
with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
