numericNameCounters = {}
currentTypeCounters = {}

def generateUserFriendlyName(type_str, id_str, isScreenshot=False):
    effectiveId = id_str if id_str else "unknown_id"
    if isScreenshot:
        key = f"Screenshot-{effectiveId}"
        if key not in numericNameCounters:
            c = currentTypeCounters.get("Screenshot", 1)
            numericNameCounters[key] = c
            currentTypeCounters["Screenshot"] = c + 1
        count = numericNameCounters[key]
        return "Screenshot" if count == 1 else f"Screenshot {count}"

    displayType = "File"
    tl = type_str.lower()
    if tl in ["image", "photo"]: displayType = "Photo"
    elif tl == "video": displayType = "Video"
    elif tl in ["audio", "music"]: displayType = "Audio"
    elif tl in ["document", "doc"]: displayType = "Document"
    else: displayType = type_str.capitalize()
    
    key = f"{displayType}-{effectiveId}"
    if key not in numericNameCounters:
        c = currentTypeCounters.get(displayType, 1)
        numericNameCounters[key] = c
        currentTypeCounters[displayType] = c + 1
    return f"{displayType} {numericNameCounters[key]}"

def cleanDisplayName(rawName, fallbackType="file", id_str=""):
    cleaned = rawName
    prefixes = ["IMG_", "VID_", "AUD_", "DOC_", "PXL_", "Screenshot_"]
    hasScreenshotPrefix = False
    for p in prefixes:
        if cleaned.lower().startswith(p.lower()):
            cleaned = cleaned[len(p):]
            if p.lower() == "screenshot_":
                hasScreenshotPrefix = True
            break
            
    lastDot = cleaned.rfind('.')
    ext = cleaned[lastDot+1:].lower() if lastDot > 0 else ""
    if lastDot > 0:
        cleaned = cleaned[:lastDot]
        
    isNumeric = all(c.isdigit() or c in '_-' for c in cleaned)
    if isNumeric or not cleaned.strip():
        typeToUse = fallbackType
        if fallbackType == "file" or not fallbackType:
            if ext in ["jpg", "jpeg", "png", "webp", "gif"]: typeToUse = "Photo"
            elif ext in ["mp4", "mkv", "avi", "mov"]: typeToUse = "Video"
            elif ext in ["mp3", "wav", "ogg", "m4a", "aac"]: typeToUse = "Audio"
            elif ext in ["pdf", "doc", "docx", "txt"]: typeToUse = "Document"
            else: typeToUse = "File"
        effectiveId = id_str if id_str else rawName
        return generateUserFriendlyName(typeToUse, effectiveId, hasScreenshotPrefix)
    return cleaned

print(cleanDisplayName("5299.jpg"))
print(cleanDisplayName("IMG_12345.jpg"))
print(cleanDisplayName("Screenshot_20231015-123456.png"))
print(cleanDisplayName("Vacation_Paris.jpg"))
print(cleanDisplayName("123.mp4"))
print(cleanDisplayName("123.mp4")) 
print(cleanDisplayName("456.mp4")) 
