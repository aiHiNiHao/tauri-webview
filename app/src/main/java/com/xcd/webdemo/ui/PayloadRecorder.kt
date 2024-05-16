package com.xcd.webdemo.ui

import android.webkit.JavascriptInterface

class PayloadRecorder {
    private val payloadMap: MutableMap<String, String> = 
        mutableMapOf()
    @JavascriptInterface
    fun recordPayload(
        method: String, 
        url: String, 
        payload: String
    ) {
        payloadMap["$method-$url"] = payload
    }
    fun getPayload(
        method: String, 
        url: String
    ): String? = 
        payloadMap["$method-$url"]
}