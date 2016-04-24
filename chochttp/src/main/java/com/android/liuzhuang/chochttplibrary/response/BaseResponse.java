package com.android.liuzhuang.chochttplibrary.response;

import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Response.
 * Created by liuzhuang on 16/3/29.
 */
public class BaseResponse {
    private String responseBody;
    /** The status code, when the server can't answer request, it will be -1*/
    private int statusCode;
    private String errorMessage;
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();

    public String getResponseBody() {
        return responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addHeader(String key, List<String> value) {
        if (CheckUtil.isEmpty(key)) {
            return;
        }
        headers.put(key.toLowerCase(), value);
    }

    public String getHeader(String key) {
        key = key.toLowerCase();
        return CheckUtil.isEmpty(headers.get(key)) ? null : headers.get(key).get(0);
    }

    public String getHeaders() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry :
                headers.entrySet()) {
            builder.append(entry.getKey()).append(":").append(entry.getValue().get(0)).append("\n");
        }
        return builder.toString();
    }
}
