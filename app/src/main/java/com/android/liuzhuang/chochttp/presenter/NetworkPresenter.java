package com.android.liuzhuang.chochttp.presenter;

import com.android.liuzhuang.chochttplibrary.ChocHttp;
import com.android.liuzhuang.chochttplibrary.IChocHttpCallback;
import com.android.liuzhuang.chochttplibrary.core.Converter;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.request.KeyValueRequest;
import com.android.liuzhuang.chochttplibrary.request.Method;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhuang on 16/3/24.
 */
public class NetworkPresenter {
//    public static final String url = "http://goodog.top:8888/";

    private NetworkCallback callback;

    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
    }

    public void sendByChocWithJson() {
        final BaseRequest request = new KeyValueRequest.Builder()
                .setUrl("http://http-caching-demo.herokuapp.com/?etag=true&cache=true, ")
                .setMethod(Method.GET)
                .build();
        ChocHttp chocHttp = new ChocHttp.Builder()
                .setConverterFactory(new GsonConverterFactory())
                .build();
        chocHttp.asyncRequest(request, new IChocHttpCallback() {
            @Override
            public void onSuccess(BaseResponse rawResponse, Object pojoResponse) {
                StringBuilder builder = new StringBuilder();
                builder.append("========URL=========\n")
                        .append(request.getRawUrl())
                        .append("\n========Headers=========\n")
                        .append(rawResponse.getHeaders())
                        .append("\n==========Body==========\n")
                        .append(rawResponse.getResponseBody());
                if (callback != null) {
                    callback.onSuccess(builder.toString());
                }
                Logger.println(pojoResponse != null && pojoResponse instanceof OutPut ? "convert success!!" : "convert failed!!");
            }

            public void onError(int statusCode, String errorMessage) {
                if (callback != null) {
                    callback.onError(statusCode + "\n" + errorMessage);
                }
            }

            public void onCanceled(BaseRequest request) {
                Logger.println("cancel " + request.getRawUrl() + "  " + request.getParams());
            }
        }, OutPut.class);
    }

    public void sendByChoc() {
        final BaseRequest request = new KeyValueRequest.Builder()
//                .setUrl("http://30.10.112.22:8080")
                .setUrl("https://kyfw.12306.cn/")
//                .setUrl("https://www.baidu.com")
//                .setUrl("http://http-caching-demo.herokuapp.com/?etag=true&cache=true, ")
                .setMethod(Method.GET)
                .build();
        ChocHttp chocHttp = new ChocHttp.Builder().build();
        chocHttp.asyncRequest(request, new IChocHttpCallback() {
            @Override
            public void onSuccess(BaseResponse rawResponse, Object pojoResponse) {
                StringBuilder builder = new StringBuilder();
                builder.append("========URL=========\n")
                        .append(request.getRawUrl())
                        .append("\n========Headers=========\n")
                        .append(rawResponse.getHeaders())
                        .append("\n==========Body==========\n")
                        .append(rawResponse.getResponseBody());
                if (callback != null) {
                    callback.onSuccess(builder.toString());
                }
            }

            public void onError(int statusCode, String errorMessage) {
                if (callback != null) {
                    callback.onError(statusCode + "\n" + errorMessage);
                }
            }

            public void onCanceled(BaseRequest request) {
                Logger.println("cancel " + request.getRawUrl() + "  " + request.getParams());
            }
        });
    }

    public static interface NetworkCallback{
        void onSuccess(String response);
        void onError(String error);
    }

    private static class GsonConverterFactory extends Converter.Factory {
        private final Gson gson;

        public GsonConverterFactory() {
            gson = new Gson();
        }

        @Override
        public Converter<String, ?> responseBodyConverter(Type type) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new ResponseConverter<>(gson, adapter);
        }
    }

    private static class ResponseConverter<T> implements Converter<String, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        public ResponseConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(String value) {
            Logger.println("========== Converter value ==========\n" + value);
            try {
                return adapter.fromJson(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class OutPut {
        @SerializedName("Apple")
        @Expose
        private List<String> Apple = new ArrayList<String>();
        @SerializedName("Dribbble")
        @Expose
        private List<String> Dribbble = new ArrayList<String>();
        @SerializedName("GitHub")
        @Expose
        private List<String> GitHub = new ArrayList<String>();
        @SerializedName("Heroku")
        @Expose
        private List<String> Heroku = new ArrayList<String>();
        @SerializedName("Gowalla")
        @Expose
        private List<String> Gowalla = new ArrayList<String>();
        @SerializedName("Square")
        @Expose
        private List<String> Square = new ArrayList<String>();
        @SerializedName("Twitter")
        @Expose
        private List<String> Twitter = new ArrayList<String>();
    }
}
