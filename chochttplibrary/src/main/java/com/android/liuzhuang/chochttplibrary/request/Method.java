package com.android.liuzhuang.chochttplibrary.request;

/**
 * Created by liuzhuang on 16/3/26.
 */
public enum Method {
    GET("GET"),
    POST("POST");

    private final String method;
    Method(String method) {
        this.method = method;
    }
}
