def is_tag_active(text, start, tag_open, tag_close):
    start = max(0, min(start, len(text)))
    if start == 0 and len(text) == 0: return False
    
    last_open = text.rfind(tag_open, 0, max(0, start))
    if last_open != -1:
        last_close_before_open = text.rfind(tag_close, 0, max(0, start))
        if last_close_before_open < last_open:
            next_close = text.find(tag_close, start)
            if next_close != -1:
                next_open = text.find(tag_open, start)
                if next_open == -1 or next_close < next_open or next_close == start:
                    return True
            else:
                return True
    return False

print(is_tag_active("<b></b>", 3, "<b>", "</b>")) # True
print(is_tag_active("<b></b>", 0, "<b>", "</b>")) # False
print(is_tag_active("<b></b>", 7, "<b>", "</b>")) # False
print(is_tag_active("<b>hello</b>", 4, "<b>", "</b>")) # True
print(is_tag_active("<b>hello</b><i>world</i>", 17, "<i>", "</i>")) # True
print(is_tag_active("<b>hello</b>", 12, "<b>", "</b>")) # False
print(is_tag_active("<b>hello</b>", 12, "<i>", "</i>")) # False
