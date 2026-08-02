import re

def get_block(filename, var_name):
    with open(filename, "r") as f:
        content = f.read()
    
    # Simple regex to extract triple quoted string assigned to var_name
    match = re.search(f'{var_name}\s*=\s*\"\"\"(.*?)\"\"\"', content, re.DOTALL)
    if match:
        return match.group(1)
    else:
        print(f"Failed to find {var_name} in {filename}")
        return ""

more_block = get_block("fix_more_and_detail.py", "new_more_block")
detail_block = get_block("fix_more_and_detail.py", "new_detail_block")
disguise_block = get_block("fix_missing_routes.py", "app_disguise_block")
backup_block = get_block("fix_backup_screen.py", "new_section")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

start_marker = '                "More" -> {'
end_marker = '                "Storage" -> {'

idx1 = content.find(start_marker)
idx2 = content.find(end_marker, idx1)

if idx1 != -1 and idx2 != -1:
    new_content = content[:idx1] + more_block + disguise_block + backup_block + detail_block + content[idx2:]
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(new_content)
    print("RESTORED EVERYTHING SUCCESSFULLY!")
else:
    print("Could not find boundaries in CalculatorScreen.kt")
