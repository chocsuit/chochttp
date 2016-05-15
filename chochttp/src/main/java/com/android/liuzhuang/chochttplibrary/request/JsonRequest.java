package com.android.liuzhuang.chochttplibrary.request;

import com.android.liuzhuang.chochttplibrary.common.Constant;
import com.android.liuzhuang.chochttplibrary.common.MediaType;

/**
 * Request in Json Format.
 * Created by liuzhuang on 16/3/29.
 */
public class JsonRequest extends BaseRequest {
    private String url;
    private String jsonString;

    public JsonRequest(Builder builder) {
        if (builder != null) {
            this.url = builder.url;
            jsonString = builder.jsonString;
        }
    }

    @Override
    public String getRawUrl() {
        return url;
    }

    /**
     * This format only support POST method
     * @return
     */
    public Method getMethod() {
        return Method.POST;
    }

    public String getParams() {
        return jsonString;
    }

    public MediaType getContentType() {
        return MediaType.parse(Constant.JSON_CONTENT_TYPE);
    }

    public static class Builder {
        private String url;
        private String jsonString;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setJsonString(String jsonString) {
            this.jsonString = jsonString;
            return this;
        }

        public JsonRequest build() {
            return new JsonRequest(this);
        }
    }
}
