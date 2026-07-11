package com.example

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testRichTextVisualTransformationOffsetMapping() {
    val transformation = RichTextVisualTransformation(Color.Magenta)
    
    val inputs = listOf(
        "",
        "Hello",
        "Hello\nWorld",
        "<b>Hello</b>",
        "<b>Hello</b>\nWorld",
        "H<b>ell</b>o",
        "<b></b>",
        "<b>x</b>",
        "<b>Hello\nWorld</b>",
        "<color hex=\"#FF6B6B\">Red text</color>",
        "<color hex=\"#FF6B6B\">Red</color> and <b>bold</b> text"
    )
    
    for (input in inputs) {
        val annotatedInput = AnnotatedString(input)
        val transformed = transformation.filter(annotatedInput)
        val transformedText = transformed.text
        val mapping = transformed.offsetMapping
        
        val n = input.length
        val tLen = transformedText.length
        
        // 1. Boundary constraints
        assertEquals("originalToTransformed(0) must be 0 for input: $input", 0, mapping.originalToTransformed(0))
        assertEquals("originalToTransformed(n) must be tLen for input: $input", tLen, mapping.originalToTransformed(n))
        assertEquals("transformedToOriginal(0) must be within bounds for input: $input", true, mapping.transformedToOriginal(0) in 0..n)
        assertEquals("transformedToOriginal(tLen) must be n for input: $input", n, mapping.transformedToOriginal(tLen))
        
        // 2. Out-of-bounds safety check
        try {
            mapping.originalToTransformed(-1)
            mapping.originalToTransformed(n + 1)
            mapping.transformedToOriginal(-1)
            mapping.transformedToOriginal(tLen + 1)
        } catch (e: Exception) {
            fail("OffsetMapping must be robust to out-of-bounds input for input: $input, threw: $e")
        }
        
        // 3. Monotonicity constraints
        var lastT = -1
        for (o in 0..n) {
            val t = mapping.originalToTransformed(o)
            assertTrue("originalToTransformed must be monotonic: o=$o, t=$t, lastT=$lastT for input: $input", t >= lastT)
            assertTrue("originalToTransformed must be within bounds: o=$o, t=$t, tLen=$tLen for input: $input", t in 0..tLen)
            lastT = t
        }
        
        var lastO = -1
        for (t in 0..tLen) {
            val o = mapping.transformedToOriginal(t)
            assertTrue("transformedToOriginal must be monotonic: t=$t, o=$o, lastO=$lastO for input: $input", o >= lastO)
            assertTrue("transformedToOriginal must be within bounds: t=$t, o=$o, n=$n for input: $input", o in 0..n)
            lastO = o
        }
        
        // 4. Invariance/consistency constraints: originalToTransformed(transformedToOriginal(t)) == t
        for (t in 0..tLen) {
            val o = mapping.transformedToOriginal(t)
            val tBack = mapping.originalToTransformed(o)
            assertEquals("Invariance constraint failed: t=$t -> o=$o -> t'=$tBack for input: $input", t, tBack)
        }
    }
  }
}

