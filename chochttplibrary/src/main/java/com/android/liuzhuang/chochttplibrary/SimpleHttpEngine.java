package com.android.liuzhuang.chochttplibrary;

import com.android.liuzhuang.chochttplibrary.common.Constant;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.request.Method;
import com.android.liuzhuang.chochttplibrary.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Based on UrlConnection
 * Created by liuzhuang on 16/3/28.
 */
public class SimpleHttpEngine {

    private static final SimpleHttpEngine instance = new SimpleHttpEngine();

    public static SimpleHttpEngine getInstance() {
        return instance;
    }

    public BaseResponse sendRequest(BaseRequest request) {
        if (request != null && request.getMethod() != null) {
            if (request.getMethod() == Method.GET) {
                return sendGetRequest(request);
            } else if (request.getMethod() == Method.POST) {
                return sendPostRequest(request);
            }
        }
        return null;
    }

    private BaseResponse sendGetRequest(BaseRequest request) {
        if (request == null) {
            throw new NullPointerException("request can not be null!");
        }
        BaseResponse response = new BaseResponse();
        try {
            URL url = request.getUrl();
            if (url == null) {
                response.setErrorMessage("URL error");
                response.setStatusCode(-1);
                return response;
            }
            String query = request.getParams();

            URLConnection connection;
            if (!CheckUtil.isEmpty(query)) {
                url = new URL(request.getRawUrl() + "?" + query);
            }
            connection = url.openConnection();
            connection.setRequestProperty("Accept-Charset", Constant.CHARSET_UTF8);

            handleResponse(response, connection);
        } catch (ConnectException e) {
            response.setErrorMessage("Connection refused");
            response.setStatusCode(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }


    private BaseResponse sendPostRequest(BaseRequest request) {
        BaseResponse response = new BaseResponse();
        try {
            URL url = request.getUrl();
            if (url == null) {
                response.setErrorMessage("URL error");
                response.setStatusCode(-1);
                return response;
            }
            String content = request.getParams();
            String contentType = request.getContentType() == null ?
                    "application/x-www-form-urlencoded" : request.getContentType().toString();
            URLConnection connection = null;

            connection = url.openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", Constant.CHARSET_UTF8);
            connection.setRequestProperty("Content-Type", contentType + ";charset=" + Constant.CHARSET_UTF8);

            OutputStream output = null;
            if (!CheckUtil.isEmpty(content)) {
                output = connection.getOutputStream();
                output.write(content.getBytes(Constant.CHARSET_UTF8));
            }

            handleResponse(response, connection);

            IOUtils.closeQuietly(output);
        } catch (ConnectException e) {
            response.setErrorMessage("Connection refused");
            response.setStatusCode(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void handleResponse(BaseResponse response, URLConnection connection) throws IOException {
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
            // store response body
            inputStream = connection.getInputStream();
            writer = new StringWriter();
            IOUtils.copy(inputStream, writer, Charset.forName(Constant.CHARSET_UTF8));
            String responseStr = writer.toString();
            response.setResponseBody(responseStr);
        } else if (respCode >= 300 && respCode < 400){
            // TODO 3XX

        }else if (respCode >= 400) {
            inputStream = ((HttpURLConnection) connection).getErrorStream();
            writer = new StringWriter();
            IOUtils.copy(inputStream, writer, Charset.forName(Constant.CHARSET_UTF8));
            String errorMsg = writer.toString();
            response.setErrorMessage(errorMsg);
        }

        IOUtils.closeQuietly(writer, inputStream);
    }
}
