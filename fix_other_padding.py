import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# "Storage"
target_storage = """                    "Storage" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {"""
replacement_storage = """                    "Storage" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {"""
content = content.replace(target_storage, replacement_storage)

# "Access Logs"
target_logs = """                    "Access Logs" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
replacement_logs = """                    "Access Logs" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
content = content.replace(target_logs, replacement_logs)

# "App Privacy"
target_privacy = """                    "App Privacy" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
replacement_privacy = """                    "App Privacy" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
content = content.replace(target_privacy, replacement_privacy)

# "Settings"
target_settings = """                    "Settings" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
replacement_settings = """                    "Settings" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
content = content.replace(target_settings, replacement_settings)

# "Security", "App Disguise", "Backup" etc.
target_security = """                    "Security" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
replacement_security = """                    "Security" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
content = content.replace(target_security, replacement_security)

target_auth = """                    "Authentication" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
replacement_auth = """                    "Authentication" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
content = content.replace(target_auth, replacement_auth)

target_protection = """                    "Protection" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
replacement_protection = """                    "Protection" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
content = content.replace(target_protection, replacement_protection)

target_shake = """                    "Shake to Exit" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
replacement_shake = """                    "Shake to Exit" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
content = content.replace(target_shake, replacement_shake)

target_monitoring = """                    "Monitoring" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
replacement_monitoring = """                    "Monitoring" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {"""
content = content.replace(target_monitoring, replacement_monitoring)

target_disguise = """                    "App Disguise" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
replacement_disguise = """                    "App Disguise" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
content = content.replace(target_disguise, replacement_disguise)

target_backup = """                    "Backup" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
replacement_backup = """                    "Backup" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {"""
content = content.replace(target_backup, replacement_backup)

target_profile = """                    "Profile" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {"""
replacement_profile = """                    "Profile" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {"""
content = content.replace(target_profile, replacement_profile)

target_recent = """                    "Recently Deleted" -> {
                        Column(modifier = Modifier.fillMaxSize()) {"""
replacement_recent = """                    "Recently Deleted" -> {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {"""
content = content.replace(target_recent, replacement_recent)

target_more = """                    "More" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {"""
replacement_more = """                    "More" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {"""
content = content.replace(target_more, replacement_more)


with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
print("Updated other crossfade padding")
