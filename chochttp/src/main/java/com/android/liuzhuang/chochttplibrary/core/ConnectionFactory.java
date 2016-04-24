package com.android.liuzhuang.chochttplibrary.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Manage creating of connection
 * Created by liuzhuang on 16/3/30.
 */
public final class ConnectionFactory {
    public static HttpURLConnection create(URL url) throws IOException {
        if (url == null) {
            throw new NullPointerException("url can not be null");
        }
        return (HttpURLConnection) url.openConnection();
    }
}
