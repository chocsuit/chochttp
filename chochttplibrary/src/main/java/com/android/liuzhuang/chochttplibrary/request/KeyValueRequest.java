package com.android.liuzhuang.chochttplibrary.request;

import com.android.liuzhuang.chochttplibrary.common.Constant;
import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.common.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link BaseRequest} with params in key-value format.
 * Created by liuzhuang on 16/3/29.
 */
public class KeyValueRequest extends BaseRequest {
    private Method method;
    private String url;
    private HashMap<String, String> params;

    private KeyValueRequest(Builder builder) {
        if (builder != null) {
            this.url = builder.url;
            this.method = builder.method;
            this.params = builder.params;
        }
    }


    @Override
    public String getRawUrl() {
        return url;
    }

    public String getParams() {
        return getQueryFromParams(params);
    }

    public Method getMethod() {
        return method;
    }

    public MediaType getContentType() {
        return MediaType.parse(Constant.FORM_CONTENT_TYPE);
    }

    private String getQueryFromParams(HashMap<String, String> params) {
        if (params != null && !params.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), Constant.CHARSET_UTF8)).append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String query = builder.toString();
            if (query.length() > 0) {
                return query.substring(0, query.length() - 1);
            }
        }
        return "";
    }

    public static class Builder{
        private Method method;
        private String url;
        private HashMap<String, String> params;

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setParams(HashMap<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder addParam(String key, String value) {
            if (CheckUtil.isEmpty(key)) {
                throw new NullPointerException("param key can not be null");
            }
            if (this.params == null) {
                this.params = new HashMap<String, String>();
            }
            params.put(key, value);
            return this;
        }

        public KeyValueRequest build() {
            return new KeyValueRequest(this);
        }
    }
}
