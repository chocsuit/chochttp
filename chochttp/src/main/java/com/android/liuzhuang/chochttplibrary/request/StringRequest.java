package com.android.liuzhuang.chochttplibrary.request;

import com.android.liuzhuang.chochttplibrary.common.Constant;
import com.android.liuzhuang.chochttplibrary.common.MediaType;
import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;

/**
 * Request in String Format. Only support POST method.</br>
 * If you want to create a JSON-String format request, create a {@link MediaType} using {@link Constant#JSON_CONTENT_TYPE}
 * Created by liuzhuang on 16/3/29.
 */
public class StringRequest extends BaseRequest {
    private String url;
    private String paramStr;
    private MediaType mediaType;

    public StringRequest(Builder builder) {
        if (builder != null) {
            this.url = builder.url;
            if (CheckUtil.isEmpty(this.url)) {
                throw new NullPointerException("URL cannot be empty!");
            }
            this.paramStr = builder.paramStr;
            this.mediaType = builder.mediaType;
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
        return paramStr;
    }

    public MediaType getContentType() {
        return mediaType == null ? MediaType.parse(Constant.FORM_CONTENT_TYPE) : mediaType;
    }

    public static class Builder {
        private String url;
        private String paramStr;
        private MediaType mediaType;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setParamStr(String paramStr) {
            this.paramStr = paramStr;
            return this;
        }

        public Builder setMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public StringRequest build() {
            return new StringRequest(this);
        }
    }
}
