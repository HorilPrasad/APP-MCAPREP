package com.mcaprep.utils

import android.webkit.JavascriptInterface

interface MathJaxInterface {
    @JavascriptInterface
    fun onMathJaxReady()
}