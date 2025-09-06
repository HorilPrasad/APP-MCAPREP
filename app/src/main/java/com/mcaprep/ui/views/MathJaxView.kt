package com.mcaprep.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mcaprep.utils.MathJaxInterface

class MathJaxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    companion object {
        private const val LOCAL_MATHJAX_PATH = "file:///android_asset/mathjax/es5/tex-mml-chtml.js"
        private const val CDN_MATHJAX_URL = "https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js"

        private const val BASE_HTML = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
                <style>
                    body {
                        margin: 0;
                        padding: 8px;
                        background-color: transparent;
                        overflow: hidden;
                    }
                    #math-container {
                        display: inline-block;
                        text-align: left;
                        font-size: 1em;
                        color: %s;
                    }
                </style>
                <script>
                    MathJax = {
                        startup: {
                            ready: () => {
                                MathJax.startup.defaultReady();
                                MathJax.startup.promise.then(() => {
                                    if (window.AndroidBridge) {
                                        window.AndroidBridge.onMathJaxReady();
                                    }
                                });
                            }
                        },
                        tex: {
                            inlineMath: [['$', '$'], ['\\(', '\\)']],
                            processEscapes: true
                        },
                        svg: {
                            fontCache: 'global',
                            scale: %f
                        },
                        options: {
                            enableMenu: false,
                            renderActions: {
                                // Disable hover tooltips
                                hover: []
                            }
                        }
                    };
                </script>
                <script id="MathJax-script" async src="%s"></script>
            </head>
            <body>
                <div id="math-container">%s</div>
            </body>
            </html>
        """
    }

    private var textColor: String = "#${Integer.toHexString(Color.BLACK).substring(2)}"
    private var scale: Float = 1.0f
    private var useLocalMathJax: Boolean = true

    init {
        setupWebView()
        setupTouchHandling()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = false
            displayZoomControls = false
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
            allowContentAccess = true
        }

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Force hide scrollbars
                view?.scrollBarStyle = SCROLLBARS_OUTSIDE_OVERLAY
                view?.overScrollMode = OVER_SCROLL_IF_CONTENT_SCROLLS
            }
        }

        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        scrollBarStyle = SCROLLBARS_OUTSIDE_OVERLAY
        overScrollMode = OVER_SCROLL_IF_CONTENT_SCROLLS
        addJavascriptInterface(object : MathJaxInterface{
            override fun onMathJaxReady() {

            }
        }, "AndroidBridge")
    }

    private fun setupTouchHandling() {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> performClick()
            }
            false
        }
        isLongClickable = false
        isHapticFeedbackEnabled = false
        setOnLongClickListener { true }
    }

    /**
     * Sets the math expression to render
     * @param mathExpression The LaTeX math expression to render
     */
    fun setText(mathExpression: String) {
        val mathJaxUrl = if (useLocalMathJax) LOCAL_MATHJAX_PATH else CDN_MATHJAX_URL
        val mathJaxContent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(mathExpression, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(mathExpression)
        }
        val html = BASE_HTML.format(textColor, scale, mathJaxUrl, mathJaxContent)

        loadDataWithBaseURL(
            if (useLocalMathJax) "file:///android_asset/" else "https://cdn.jsdelivr.net/",
            html,
            "text/html",
            "UTF-8",
            null
        )
    }

    /**
     * Sets the text color for the math expressions
     * @param color Hex color string (e.g. "#RRGGBB") or Android color int
     */
    fun setTextColor(color: Any) {
        textColor = when (color) {
            is Int -> "#${Integer.toHexString(color).substring(2)}"
            is String -> if (color.startsWith("#")) color else "#$color"
            else -> "#000000"
        }
    }

    /**
     * Sets the scale factor for the rendered math
     * @param scale Scale factor (default is 1.0)
     */
    fun setScale(scale: Float) {
        this.scale = scale.coerceAtLeast(0.1f)
    }

    /**
     * Toggle between local MathJax assets or CDN
     * @param useLocal true to use local assets (must be included in assets folder)
     */
    fun useLocalMathJax(useLocal: Boolean) {
        this.useLocalMathJax = useLocal
    }

    /**
     * Clears the current math expression
     */
    fun clear() {
        loadUrl("about:blank")
    }

    /**
     * Call this from onDestroy to cleanup
     */
    fun destroyView() {
        clear()
        destroy()
    }
}