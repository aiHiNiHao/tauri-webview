package com.xcd.webdemo.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.activity.ComponentActivity
import com.xcd.webdemo.R
import okhttp3.OkHttpClient
import java.io.ByteArrayInputStream


class WebActivity : ComponentActivity() {
    var webView: WebView? = null
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)

        val webSettings = webView?.settings ?: return
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)
//        cookieManager.setCookie("https://trader.chimchim.top/", "locale=en-US; mailchimp_landing_site=https%3A%2F%2Ftrader.chimchim.top%2F%3Fcode%3D_e_uvXc0S0nBi5LJY-QGNZNe1OCO0lXrEHpWmX6IsApT0sNdPc4Qw80mJUElqHgYoaK0A9_o6SG0Q7z87K-w8oSJBU4eptMXWLyQdf9uBrYifOq7lhBAAaWtsWRI--to; woocommerce_items_in_cart=1; woocommerce_cart_hash=d56e2adce030a4f33a21f250ed5b9748; wp_woocommerce_session_c9271533bf760e8ee0d914e46b63bd10=26%7C%7C1715847922%7C%7C1715844322%7C%7C1d57e19a2d48162824c791f7a3911bcd; sidebarStatus=1; wordpress_test_cookie=WP%20Cookie%20check; mo_oauth_login_app_session=none")
//        generateCookie(cookieManager)


//        // 设置编码
//        // 设置编码
//        webSettings.builtInZoomControls = false
//        webSettings.setSupportZoom(false)
//        webSettings.displayZoomControls = false
//        webSettings.useWideViewPort = true
//        webSettings.loadWithOverviewMode = true
//        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.javaScriptEnabled = true // 为WebView使能JavaScript
//
//        // 设置可以访问文件
//        // 设置可以访问文件
        webSettings.allowFileAccess = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webView!!.requestFocus()


        webView!!.addJavascriptInterface(XHRHandler(), "XHRHandler")

        webView?.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                injectXHRHandlingJS()
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                //拦截网页端的验证请求，
                if (request.url.path?.contains("security_check") == true) {
                    return WebResourceResponse(
                        "text/html", "utf-8", 200, "OK", hashMapOf(), ByteArrayInputStream(
                            byteArrayOf()
                        )
                    )
                }

                return super.shouldInterceptRequest(view, request)
            }

        }

        webView?.loadUrl("https://trader.chimchim.top/client/")
//        findViewById<Button>(R.id.refresh).setOnClickListener {
//            webView?.loadUrl("https://trader.chimchim.top/client/")
//        }
    }

    private fun injectXHRHandlingJS() {

        // 注入 JavaScript 代码拦截 XHR 请求，
        webView!!.evaluateJavascript(
            "if (!window.alreadyInjected) {" +
                    "(function() {" +
                    "    var originalXHROpen = window.XMLHttpRequest.prototype.open;" +
                    "    window.XMLHttpRequest.prototype.open = function(method, url) {" +
                    "        console.log('Intercepted XHR request:', method, url);" +
                    "        if (url.includes('/security_check')) {" +
                    "            console.log('Intercepted XHR request:', method, url);" +
                    "            this.recordedMethod = method;" +
                    "            this.recordedUrl = url;" +
                    "            var assessToken = window.localStorage.getItem(\"Admin-Token\");" +
                    "            window.XHRHandler.handleXHR(JSON.stringify({ method: method, url: url, assessToken: assessToken}));" +
                    "        } else if (url.includes('/sign-out')) {" +
                    "            window.XHRHandler.handleXHRSignOut()" +
                    "        };" +
                    "        return originalXHROpen.apply(this, arguments);" +
                    "    };" +
                    "    var originalXHRSend = window.XMLHttpRequest.prototype.send;" +
                    "    window.XMLHttpRequest.prototype.send = function(body) {" +
                    "        if (this.recordedMethod && this.recordedUrl && this.recordedMethod.toLowerCase() === 'post') {" +
                    "            var assessToken = window.localStorage.getItem(\"Admin-Token\");" +
                    "            if (body instanceof FormData) {" +
                    "                var formData = {};" +
                    "                for (var pair of body.entries()) {" +
                    "                    formData[pair[0]] = pair[1];" +
                    "                }" +
                    "            console.log('Intercepted XHR request11111:', JSON.stringify(formData), assessToken);" +

                    "                window.XHRHandler.handleXHRWithFormData(this.recordedUrl, this.recordedMethod, assessToken, JSON.stringify(formData));" +
                    "            } else {" +
                    "            console.log('Intercepted XHR request2222:', JSON.stringify(body), assessToken);" +

                    "                window.XHRHandler.handleXHRWithBody(this.recordedUrl, this.recordedMethod, assessToken, JSON.stringify(body));" +
                    "            }" +
                    "        }" +
                    "        return originalXHRSend.apply(this, arguments);" +
                    "    };" +
                    "})();" +
                    "window.alreadyInjected = true;" +
                    "}"
        ) {
            Log.i("---+++---", "injectJs: $it")
        }
    }

}