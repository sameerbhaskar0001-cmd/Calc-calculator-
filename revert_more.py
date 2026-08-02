import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

start_marker_more = '                "More" -> {'
end_marker_more = '                "Backup" -> {'

idx1_more = content.find(start_marker_more)
idx2_more = content.find(end_marker_more, idx1_more)

if idx1_more != -1 and idx2_more != -1:
    more_block = content[idx1_more:idx2_more]
    
    # Replace scoreColor
    old_color_block = """                        val scoreColor = when {
                            animatedScore >= 90 -> Color(0xFF10B981) // Emerald Green
                            animatedScore >= 70 -> Color(0xFF0EA5E9) // Blue Cyan
                            animatedScore >= 50 -> Color(0xFFF59E0B) // Warm Orange
                            else -> Color(0xFFEF4444) // Soft Red
                        }"""
    
    new_color_block = """                        val scoreColor = when {
                            animatedScore >= 95 -> Color(0xFF00E676)
                            animatedScore >= 80 -> Color(0xFF69F0AE)
                            animatedScore >= 60 -> Color(0xFFFFC107)
                            animatedScore >= 40 -> Color(0xFFFF9800)
                            else -> Color(0xFFFF5252)
                        }"""
                        
    more_block = more_block.replace(old_color_block, new_color_block)
    
    # Replace scoreText
    old_text_block = """                        val scoreText = when {
                            animatedScore >= 90 -> "Excellent Security"
                            animatedScore >= 70 -> "Strong Protection"
                            animatedScore >= 50 -> "Good Security"
                            else -> "Needs Improvement"
                        }"""
                        
    new_text_block = """                        val scoreText = when {
                            animatedScore >= 95 -> "Excellent Security"
                            animatedScore >= 80 -> "Strong Protection"
                            animatedScore >= 60 -> "Good Security"
                            animatedScore >= 40 -> "Needs Improvement"
                            else -> "Weak Protection"
                        }"""
                        
    more_block = more_block.replace(old_text_block, new_text_block)

    content = content[:idx1_more] + more_block + content[idx2_more:]
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(content)
    print("Reverted More block colors.")
else:
    print("Failed to find boundaries.")
