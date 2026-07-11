package com.example

import android.net.Uri
import org.junit.Test
import org.junit.Assert.*

class UriTest {
    @Test
    fun testUri() {
        val uriStr = "content://media/picker/0/com.android.providers.media.photopicker/media/1000000000"
        val uri = Uri.parse(uriStr)
        println(uri.lastPathSegment)
    }
}
