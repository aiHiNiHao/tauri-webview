package com.xcd.webdemo.ui;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomWebViewClient extends WebViewClient {

    private OkHttpClient okHttpClient;

    public CustomWebViewClient() {
        // 初始化OkHttpClient，你可以在这里添加拦截器等配置
        okHttpClient = new OkHttpClient.Builder()
                .build();
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (request.getUrl().toString().equals("https://trader.chimchim.top/security_check")
        ) {// 根据需要处理GET或POST请求
            return handleRequestWithOkHttp(request);
        }
        return super.shouldInterceptRequest(view, request);
    }

    private WebResourceResponse handleRequestWithOkHttp(WebResourceRequest request) {

        try {

            // 创建新的请求Builder，并从原请求中复制信息
            Request okHttpRequest = new Request.Builder()
                    .url(request.getUrl().getPath())
                    .headers(overrideHeaders(request.getRequestHeaders())) // 修改请求头
                    .build();

            // 使用OkHttp发送请求
            Response okHttpResponse = okHttpClient.newCall(okHttpRequest).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Map<String, String> emptyHeaders = new HashMap<>();
        WebResourceResponse emptyWebResourceResponse = new WebResourceResponse("text/html", "UTF-8", emptyInputStream);
        emptyWebResourceResponse.getResponseHeaders().putAll(emptyHeaders);
        return emptyWebResourceResponse;
    }

    private Headers overrideHeaders(Map<String, String> originalHeaders) {
        Headers.Builder headersBuilder = new Headers.Builder();
        // 遍历并添加原有头部信息
        for (Map.Entry<String, String> entry : originalHeaders.entrySet()) {
            headersBuilder.add(entry.getKey(), entry.getValue());
        }
        // 添加或修改Cookie
        headersBuilder.add("Cookie", "mailchimp_landing_site=https%3A%2F%2Ftrader.chimchim.top%2F; sbjs_migrations=1418474375998%3D1; sbjs_current_add=fd%3D2024-05-13%2007%3A23%3A02%7C%7C%7Cep%3Dhttps%3A%2F%2Ftrader.chimchim.top%2F%7C%7C%7Crf%3D%28none%29; sbjs_first_add=fd%3D2024-05-13%2007%3A23%3A02%7C%7C%7Cep%3Dhttps%3A%2F%2Ftrader.chimchim.top%2F%7C%7C%7Crf%3D%28none%29; sbjs_current=typ%3Dtypein%7C%7C%7Csrc%3D%28direct%29%7C%7C%7Cmdm%3D%28none%29%7C%7C%7Ccmp%3D%28none%29%7C%7C%7Ccnt%3D%28none%29%7C%7C%7Ctrm%3D%28none%29%7C%7C%7Cid%3D%28none%29; sbjs_first=typ%3Dtypein%7C%7C%7Csrc%3D%28direct%29%7C%7C%7Cmdm%3D%28none%29%7C%7C%7Ccmp%3D%28none%29%7C%7C%7Ccnt%3D%28none%29%7C%7C%7Ctrm%3D%28none%29%7C%7C%7Cid%3D%28none%29; locale=en-US; language=en; sidebarStatus=1; wordpress_test_cookie=WP%20Cookie%20check; mo_oauth_login_app_session=none; sbjs_udata=vst%3D1%7C%7C%7Cuip%3D%28none%29%7C%7C%7Cuag%3DMozilla%2F5.0%20%28Linux%3B%20Android%2013%3B%20Pixel%207%29%20AppleWebKit%2F537.36%20%28KHTML%2C%20like%20Gecko%29%20Chrome%2F116.0.0.0%20Mobile%20Safari%2F537.36%20Edg%2F124.0.0.0; sbjs_session=pgs%3D4%7C%7C%7Ccpg%3Dhttps%3A%2F%2Ftrader.chimchim.top%2F"); // 这里替换为你的Cookie值
        return headersBuilder.build();
    }
}