package com.android.liuzhuang.chochttp.presenter;

import com.android.liuzhuang.chochttplibrary.ChocHttp;
import com.android.liuzhuang.chochttplibrary.IChocHttpCallback;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.request.KeyValueRequest;
import com.android.liuzhuang.chochttplibrary.request.Method;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.Logger;

/**
 * Created by liuzhuang on 16/3/24.
 */
public class NetworkPresenter {
//    public static final String url = "http://goodog.top:8888/";

    private NetworkCallback callback;

    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
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
}
