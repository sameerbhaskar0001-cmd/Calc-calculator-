package com.example
import org.mozilla.geckoview.GeckoSession

class TestDelegate : GeckoSession.ContentDelegate {
    override fun onCrash(session: GeckoSession) {
    }
}
