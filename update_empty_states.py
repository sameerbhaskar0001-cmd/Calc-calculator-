with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

for i in range(len(lines)):
    if 'EmptyVaultSectionState(' in lines[i]:
        if 'title = "No Secure Notes"' in lines[i+1]:
            lines[i+2] = lines[i+2].rstrip() + ',\n                                actionLabel = "Add Note",\n                                onActionClick = { showAddNoteDialog = true }\n'
        elif 'title = "No Photos or Videos"' in lines[i+1]:
            lines[i+2] = lines[i+2].rstrip() + ',\n                                actionLabel = "Add Media",\n                                onActionClick = { showMediaAddOptions = true }\n'
        elif 'title = "No Documents"' in lines[i+1]:
            lines[i+2] = lines[i+2].rstrip() + ',\n                                actionLabel = "Add Document",\n                                onActionClick = { showDocAddOptions = true }\n'
        elif 'title = "No Music or Audio Files"' in lines[i+1]:
            lines[i+2] = lines[i+2].rstrip() + ',\n                                actionLabel = "Add Audio",\n                                onActionClick = { audioPickerLauncher.launch("audio/*") }\n'

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(lines)
