import re

with open("app/src/main/java/com/example/CalculatorScreen.kt") as f:
    text = f.read()

# I will replace Modifier.size(36.dp) with the proper one.
# But I must be careful to only do this for the rich text toolbar.
# Let's find the Row for the toolbar.
start_idx = text.find("editedNoteContentValue = toggleTag(editedNoteContentValue, \"<b>\", \"</b>\")")
if start_idx == -1:
    print("Could not find toolbar")
    exit(1)

# We will just do a regex replace restricted to a substring around start_idx
end_idx = text.find("OutlinedTextField(", start_idx)

sub = text[start_idx-200:end_idx]

sub = re.sub(
    r"Modifier\.size\(36\.dp\)(?=\s*\)\s*\{\s*Text\(\"B\")",
    r"Modifier.size(36.dp).background(if (isTagActive(editedNoteContentValue, \"<b>\", \"</b>\")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))",
    sub
)

sub = re.sub(
    r"Modifier\.size\(36\.dp\)(?=\s*\)\s*\{\s*Text\(\"I\")",
    r"Modifier.size(36.dp).background(if (isTagActive(editedNoteContentValue, \"<i>\", \"</i>\")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))",
    sub
)

sub = re.sub(
    r"Modifier\.size\(36\.dp\)(?=\s*\)\s*\{\s*Text\(\"U\")",
    r"Modifier.size(36.dp).background(if (isTagActive(editedNoteContentValue, \"<u>\", \"</u>\")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))",
    sub
)

sub = re.sub(
    r"Modifier\.size\(36\.dp\)(?=\s*\)\s*\{\s*Icon\(Icons\.Default\.List)",
    r"Modifier.size(36.dp).background(if (isPrefixActive(editedNoteContentValue, \"• \")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))",
    sub
)

sub = re.sub(
    r"Modifier\.size\(36\.dp\)(?=\s*\)\s*\{\s*Text\(\"1\.\")",
    r"Modifier.size(36.dp).background(if (isPrefixActive(editedNoteContentValue, \"1. \")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))",
    sub
)

sub = re.sub(
    r"Modifier\.size\(36\.dp\)(?=\s*\)\s*\{\s*Icon\(Icons\.Default\.CheckBox)",
    r"Modifier.size(36.dp).background(if (isPrefixActive(editedNoteContentValue, \"[ ] \")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))",
    sub
)

text = text[:start_idx-200] + sub + text[end_idx:]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)
    
print("Updated modifiers")
