package com.android.liuzhuang.chochttplibrary.response;

import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.common.Headers;

import java.util.List;

/**
 * Created by liuzhuang on 16/3/29.
 */
public class BaseResponse {
    private String responseBody;
    /** The status code, when the server can't answer request, it will be -1*/
    private int statusCode;
    private String errorMessage;
    private Headers headers;

    public String getResponseBody() {
        return responseBody;
    }

    public Headers getHeaders() {
        return headers;
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
        if (headers == null) {
            headers = new Headers();
        }
        headers.addHeader(key, value);
    }
}
