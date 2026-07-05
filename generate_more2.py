import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

start_index = content.find('"More" -> {\n                    // Initialize inputs on Settings entry')
if start_index == -1:
    print("Start not found")
else:
    # Find matching brace
    brace_count = 0
    end_index = -1
    for i in range(start_index + 12, len(content)):
        if content[i] == '{':
            brace_count += 1
        elif content[i] == '}':
            brace_count -= 1
            if brace_count == 0:
                end_index = i
                break
    
    if end_index != -1:
        old_more_block = content[start_index:end_index + 1]
        print(f"Found block from {start_index} to {end_index}")
        
        # Write the new blocks
        with open("new_blocks.txt", "r") as f2:
            new_blocks = f2.read()
            
        content = content[:start_index] + new_blocks + content[end_index + 1:]
        with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f3:
            f3.write(content)
        print("Replaced successfully")
    else:
        print("End not found")
