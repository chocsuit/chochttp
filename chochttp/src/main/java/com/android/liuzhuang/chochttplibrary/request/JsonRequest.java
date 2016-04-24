package com.android.liuzhuang.chochttplibrary.request;

import com.google.gson.Gson;
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

        /**
         * parse POJO to json String by GSON, <strong>this means that the pojo must follow GSON rules.
         * <a href="https://github.com/google/gson/blob/master/UserGuide.md">https://github.com/google/gson/blob/master/UserGuide.md</a></strong> <br>
         * You can just call {@link #setJsonString(String)} if using custom JSON library.
         * @param pojo
         * @return
         */
        public Builder setPOJO(Object pojo) {
            if (pojo == null) {
                throw new NullPointerException("POJO can not be null!");
            }
            Gson gson = new Gson();
            this.jsonString = gson.toJson(pojo);
            return this;
        }

        public JsonRequest build() {
            return new JsonRequest(this);
        }
    }
}
