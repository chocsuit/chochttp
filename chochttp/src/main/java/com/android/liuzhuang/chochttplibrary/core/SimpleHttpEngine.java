package com.android.liuzhuang.chochttplibrary.core;

import com.android.liuzhuang.chochttplibrary.cache.CacheEngine;
import com.android.liuzhuang.chochttplibrary.common.Constant;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.request.Method;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.utils.IOUtils;
import com.android.liuzhuang.chochttplibrary.utils.Logger;
import com.android.liuzhuang.chochttplibrary.utils.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Based on UrlConnection
 * Created by liuzhuang on 16/3/28.
 */
public class SimpleHttpEngine {

    private static final SimpleHttpEngine instance = new SimpleHttpEngine();

    public static SimpleHttpEngine getInstance() {
        return instance;
    }

    private CacheEngine cacheEngine = new CacheEngine();

    private Timer timer = new Timer();

    public BaseResponse sendRequest(BaseRequest request) {
        timer.start();

        BaseResponse response = cacheEngine.createResponse(request.getRawUrl());
        // need not to request server.
        if (!cacheEngine.isExpired(response)) {
            Logger.println("=========get from local========");
            timer.end("network locally");
            return response;
        }

        wrapRequestByCache(request, response);

        if (response == null) {
            response = new BaseResponse();
        }
        if (request.getMethod() != null) {
            if (request.getMethod() == Method.GET) {
                return sendGetRequest(request, response);
            } else if (request.getMethod() == Method.POST) {
                return sendPostRequest(request, response);
            }
        }
        return null;
    }

    private void wrapRequestByCache(BaseRequest request, BaseResponse response) {
        if (response == null) {
            return;
        }
        if (request == null) {
            throw new NullPointerException("request or response can not be null!");
        }
        String lastModified = response.getHeader(Constant.HEADER_LAST_MODIFIED);
        if (!CheckUtil.isEmpty(lastModified)) {
            request.ifModifiedSince = lastModified;
        }
        String etag = response.getHeader(Constant.HEADER_ETAG);
        if (!CheckUtil.isEmpty(etag)) {
            request.ifNoneMatch = etag;
        }
    }

    private BaseResponse sendGetRequest(BaseRequest request, BaseResponse response) {
        if (request == null) {
            throw new NullPointerException("request can not be null!");
        }
        URLConnection connection = null;
        try {
            URL url = request.getUrl();
            if (url == null) {
                response.setErrorMessage("URL error");
                response.setStatusCode(-1);
                return response;
            }

            connection = ConnectionFactory.create(url);
            wrapConnectionByRequest(connection, request);

            connection.setRequestProperty("Accept-Charset", Constant.CHARSET_UTF8);

            handleResponse(request.getRawUrl(), response, connection);
        } catch (ConnectException e) {
            response.setErrorMessage("Connection refused");
            response.setStatusCode(-1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ((HttpURLConnection) connection).disconnect();
            }
        }

        return response;
    }

    private BaseResponse sendPostRequest(BaseRequest request, BaseResponse response) {
        URLConnection connection = null;
        try {
            URL url = request.getUrl();
            if (url == null) {
                response.setErrorMessage("URL error");
                response.setStatusCode(-1);
                return response;
            }

            connection = ConnectionFactory.create(url);
            wrapConnectionByRequest(connection, request);

            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", Constant.CHARSET_UTF8);

            String contentType = request.getContentType() == null ?
                    "application/x-www-form-urlencoded" : request.getContentType().toString();
            connection.setRequestProperty("Content-Type", contentType + ";charset=" + Constant.CHARSET_UTF8);

            OutputStream output = null;
            String content = request.getParams();
            if (!CheckUtil.isEmpty(content)) {
                output = connection.getOutputStream();
                output.write(content.getBytes(Constant.CHARSET_UTF8));
            }

            handleResponse(request.getRawUrl(), response, connection);

            IOUtils.closeQuietly(output);
        } catch (ConnectException e) {
            response.setErrorMessage("Connection refused");
            response.setStatusCode(-1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                ((HttpURLConnection) connection).disconnect();
            }
        }
        return response;
    }

    private void handleResponse(String rawUrl, BaseResponse response, URLConnection connection) throws IOException {
        // store header
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            response.addHeader(header.getKey(), header.getValue());
        }

        // Store response code
        int respCode = ((HttpURLConnection) connection).getResponseCode();
        response.setStatusCode(respCode);

        InputStream inputStream = null;
        StringWriter writer = null;

        if (respCode >= 200 && respCode < 300){
            // get response body
            inputStream = connection.getInputStream();
            writer = new StringWriter();
            IOUtils.copy(inputStream, writer, Charset.forName(Constant.CHARSET_UTF8));
            String responseStr = writer.toString();
            response.setResponseBody(responseStr);
            // cache
            cacheEngine.save2cache(rawUrl, response);
        } else if (respCode == HttpURLConnection.HTTP_NOT_MODIFIED){
            // cache
            cacheEngine.save2cache(rawUrl, response);
        }else if (respCode >= 400) {
            inputStream = ((HttpURLConnection) connection).getErrorStream();
            writer = new StringWriter();
            IOUtils.copy(inputStream, writer, Charset.forName(Constant.CHARSET_UTF8));
            String errorMsg = writer.toString();
            response.setErrorMessage(errorMsg);
        }
        Logger.println("=========get from remote========");
        Logger.println("responseCode==>>" + respCode);
        IOUtils.closeQuietly(writer, inputStream);
        timer.end("network remotely");
    }

    private void wrapConnectionByRequest(URLConnection connection, BaseRequest request) {
        if (connection == null || request == null) {
            return;
        }
        if (!CheckUtil.isEmpty(request.ifModifiedSince) || !CheckUtil.isEmpty(request.ifNoneMatch)) {
            connection.setUseCaches(true);
        }
        if (!CheckUtil.isEmpty(request.ifModifiedSince)) {
            try {
                connection.setIfModifiedSince(Long.parseLong(request.ifModifiedSince.trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (!CheckUtil.isEmpty(request.ifNoneMatch)) {
            connection.setRequestProperty(Constant.HEADER_IF_NONE_MATCH, request.ifNoneMatch);
        }
        if (request.contentLength >= 0 && connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setFixedLengthStreamingMode(request.contentLength);
        }
        if (request.chunkLength >= 0 && connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setChunkedStreamingMode(request.chunkLength);
        }
        if (connection instanceof HttpURLConnection) {
            try {
                HttpsSupport.trustAllCerts((HttpURLConnection) connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
