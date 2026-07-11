class RichTextVisualTransformation(private val themePurple: Color) : VisualTransformation {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RichTextVisualTransformation) return false
        return themePurple == other.themePurple
    }
    
    override fun hashCode(): Int {
        return themePurple.hashCode()
    }
    
    override fun filter(text: AnnotatedString): TransformedText {
        try {
            val rawText = text.text
