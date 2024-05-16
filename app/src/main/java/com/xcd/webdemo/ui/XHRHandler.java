package com.xcd.webdemo.ui;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// JavaScript interface for handling XHR requests
/**
 * 问题原因是 webview 添加了请求头("X-Requested-With", "com.xcd.webdemo")，如果又更简单的方案控制webview不添加这个请求头，这里的代码就可以退役了
 **/
class XHRHandler {
    private String TAG = "---+++";
    CookieManager cookieManager = CookieManager.getInstance();
    OkHttpClient client = null;

    Set<String> handles = new HashSet<>();

    @JavascriptInterface
    public void handleXHRSignOut() {
//        Log.e(TAG, "handleXHRSignOut");
        String domain = "trader.chimchim.top";
        String cookie = "=; expires=Thu, 01 Jan 1970 00:00:00 GMT; domain=" + domain;
        cookieManager.setCookie(domain, cookie);

        // 强制使Cookie变更生效
        cookieManager.removeExpiredCookie();
        cookieManager.flush();
        String cookie1 = cookieManager.getCookie("trader.chimchim.top");
        handles.clear();
//        Log.i(TAG, "handleXHRSignOut: cookie  :" + cookie1);
    }

    @JavascriptInterface
    public void handleXHR(String xhrDataJson) {
//        Log.d(TAG, "XHR request intercepted: " + xhrDataJson);
        // Process XHR request without request body
    }

    public void handle3(String url, String assessToken, String session) {
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("Accept-Encoding", "gzip, deflate, br, zstd")
                .add("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .add("Connection", "keep-alive")
                .set("Cookie", "locale=en-US; " + session + "; sidebarStatus=0")
                .add("Host", "trader.chimchim.top")
                .add("Istoken", "true")
                .add("Redirect-Path", "https://trader.chimchim.top/oauth2/authorize?client_id=PtClientId&redirect_uri=https://trader.chimchim.top&response_type=code&scope=openid%20profile%20message.read%20message.write")
                .set("Referer", "https://trader.chimchim.top/client/login?redirect=/challenge")
//              .add(          "Sec-Ch-Ua", "\"Chromium\";v=\"124\", \"Microsoft Edge\";v=\"124\", \"Not-A.Brand\";v=\"99\"",
                .add("Sec-Ch-Ua-Mobile", "?1")
                .add("Sec-Ch-Ua-Platform", "\"Android\"")
                .add("Sec-Fetch-Dest", "empty")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Site", "same-origin")
//              .add(          "User-Agent", "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36 Edg/124.0.0.0",
                .add("X-Authorization", assessToken)
                .add("X-Locale", "en-US")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        // 发送请求并获取响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 响应成功
                    System.out.println(response.body().string());
                } else {
                    // 响应失败
                    System.out.println("Request failed: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败
                e.printStackTrace();
            }
        });
    }

    public void handle2(String url, String assessToken, String session) {
        // 构建请求头
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("Accept-Encoding", "gzip, deflate, br, zstd")
                .add("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .add("Connection", "keep-alive")
                .set("Cookie", "locale=en-US; " + session + "; sidebarStatus=0")
                .add("Host", "trader.chimchim.top")
                .add("Istoken", "true")
                .add("Redirect-Path", "https://trader.chimchim.top/oauth2/authorize?client_id=PtClientId&redirect_uri=https://trader.chimchim.top&response_type=code&scope=openid%20profile%20message.read%20message.write")
                .set("Referer", "https://trader.chimchim.top/client/login?redirect=/challenge")
//                .add("Sec-Ch-Ua", "\"Chromium\";v=\"124\", \"Microsoft Edge\";v=\"124\", \"Not-A.Brand\";v=\"99\"")
                .add("Sec-Ch-Ua-Mobile", "?1")
                .add("Sec-Ch-Ua-Platform", "\"Android\"")
                .add("Sec-Fetch-Dest", "empty")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Site", "same-origin")
//                .add("User-Agent", "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36 Edg/124.0.0.0")
                .add("X-Authorization", assessToken)
                .add("X-Locale", "en-US")
                .build();

        // 构建请求
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        // 发送请求并获取响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 响应成功
                    System.out.println(response.body().string());
                } else {
                    // 响应失败
                    System.out.println("Request failed: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败
                e.printStackTrace();
            }
        });
    }

    @JavascriptInterface
    public void handleXHRWithFormData(String url, String method, String assessToken, String formDataJson) {
//        Log.d(TAG, "XHR request intercepted - URL: " + url + ", Method: " + method + ", assessToken: " + assessToken);
//        Log.d(TAG, "Form data: " + formDataJson);
        if (client == null) {
            handles.clear();
            client = new OkHttpClient.Builder()
                    .followRedirects(false) // 禁用自动重定向
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            List<String> cookies = response.headers("Set-Cookie");
                            String session = "";

                            for (String cookie : cookies) {
                                if (cookie.startsWith("SESSION")) {
                                    session = cookie.split(";")[0];
                                }
                                cookieManager.setCookie("https://trader.chimchim.top/", cookie);
                            }
                            // 检查重定向
                            if (response.isRedirect()) {
                                String redirectUrl = response.header("Location");
                                if (!handles.contains(redirectUrl)) {
                                    handles.add(redirectUrl);
                                    String headerStr = response.request().headers().toString();
                                    if (redirectUrl.startsWith("https://trader.chimchim.top/oauth2/authorize")) {
                                        handle2(redirectUrl, assessToken, session);
                                        Log.i(TAG, "intercept: header1 == " + headerStr);
                                    } else if (redirectUrl.startsWith("https://trader.chimchim.top?code=")) {
                                        handle3(redirectUrl, assessToken, session);
                                        Log.i(TAG, "intercept: header2222 == " + headerStr);
                                    } else if (redirectUrl.equals("https://trader.chimchim.top")) {
                                        Log.i(TAG, "intercept: " + redirectUrl);
                                        Log.i(TAG, "intercept: header3333 == " + headerStr);
                                    }

                                }
                            }

                            return response;
                        }
                    }) // 添加重定向拦截器
                    .build();
        }


        // 构造表单数据 {"username":"candy201268@163.com","password":"Aa123456"}
        String username = "";
        String password = "";
        try {
            JSONObject jsonObject = new JSONObject(formDataJson);

            username = jsonObject.getString("username");
            password = jsonObject.getString("password");

            Log.d(TAG, "username: " + username);
            Log.d(TAG, "password: " + password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("Accept-Encoding", "gzip, deflate, br, zstd")
                .add("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .add("Connection", "keep-alive")
                .add("Content-Length", "261")
                .add("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary1EflmmRjtIOLAGFb")
                .set("Cookie", "locale=en-US")
                .add("Host", "trader.chimchim.top")
                .add("Istoken", "true")
                .add("Origin", "https://trader.chimchim.top")
                .add("Redirect-Path", "https://trader.chimchim.top/oauth2/authorize?client_id=PtClientId&redirect_uri=https://trader.chimchim.top&response_type=code&scope=openid%20profile%20message.read%20message.write")
                .set("Referer", "https://trader.chimchim.top/client/login?redirect=/challenge")
//                .add("Sec-Ch-Ua", "\"Chromium\";v=\"124\", \"Microsoft Edge\";v=\"124\", \"Not-A.Brand\";v=\"99\"")
                .add("Sec-Ch-Ua-Mobile", "?1")
                .add("Sec-Ch-Ua-Platform", "\"Android\"")
                .add("Sec-Fetch-Dest", "empty")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Site", "same-origin")
//                .add("User-Agent", "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36 Edg/124.0.0.0")
                .add("X-Authorization", assessToken)
                .add("X-Locale", "en-US")
//                .add("X-Requested-With", "com.xcd.webdemo")
                .build();

        // 构造 POST 请求
        Request request = new Request.Builder()
                .url("https://trader.chimchim.top/security_check")
                .post(formBody)
                .headers(headers)
                .build();

        // 发起请求并异步处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // 获取响应体
                String responseData = response.body().string();
                System.out.println(responseData);
            }
        });
    }


    @JavascriptInterface
    public void handleXHRWithBody(String url, String method, String assessToken, String requestBodyJson) {
//        Log.d(TAG, "XHR request intercepted - URL: " + url + ", Method: " + method);
//        Log.d(TAG, "Request body: " + requestBodyJson);

        // Use OkHttp to handle the intercepted XHR request
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);

        // Set up request method and body
        if (method.equalsIgnoreCase("POST")) {
            requestBuilder.post(RequestBody.create(MediaType.parse("application/json"), requestBodyJson));
        } else {
            requestBuilder.method(method, null);
        }

        // Build the request
        Request request = requestBuilder.build();

        // Asynchronous call using OkHttp
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "OkHttp request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                // Now you can handle the response data as needed
//                Log.d(TAG, "OkHttp response: " + responseData);
            }
        });
    }

}
