package com.android.liuzhuang.chochttp.presenter;

import com.android.liuzhuang.chochttplibrary.ChocHttp;
import com.android.liuzhuang.chochttplibrary.IChocHttpCallback;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.request.KeyValueRequest;
import com.android.liuzhuang.chochttplibrary.request.Method;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.Logger;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuzhuang on 16/3/30.
 */
public class ChocHttpTest {
    public static void main(String[] args) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key1", "中文");
        params.put("key2", "value2");
        params.put("key3", "value3");
        params.put("key4", "value4");
        params.put("key5", "value5");
        ChocHttp<POJO> chocHttp = new ChocHttp<POJO>();
        List<BaseRequest> requests = new ArrayList<BaseRequest>();
//        BaseRequest request = new KeyValueRequest.Builder()
//                .setUrl("http://localhost:8888/")
////                    .setUrl("https://www.baidu.com/")
//                .setMethod(Method.POST)
////                    .setParams(params)
//                .addParam("key", "value")
//                .build();
        for (int i = 0; i < 1; i++) {
            BaseRequest request = new KeyValueRequest.Builder()
                    .setUrl("http://30.10.119.190:8888/")
//                    .setUrl("https://www.baidu.com/")
                    .setMethod(Method.POST)
//                    .setParams(params)
                    .addParam("key" + i, "value" + i)
                    .build();
            chocHttp.asyncRequest(request, new IChocHttpCallback<POJO>() {
                public void onSuccess(BaseResponse rawResponse, POJO pojoResponse) {
                    Logger.println(rawResponse.getResponseBody());
                    Logger.println(pojoResponse.key0);
                }

                public void onError(int statusCode, String errorMessage) {
                    Logger.println(statusCode + "\n" + errorMessage);
                }

                public void onCanceled(BaseRequest request) {
                    Logger.println("cancel " + request.getRawUrl() + "  " + request.getParams());
                }
            }, POJO.class);
            requests.add(request);
//            try {
//                Thread.sleep(100);
//                if (i == 5) {
//                    chocHttp.cancelAll();
//                    return;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
//        chocHttp.cancelAll();
//        chocHttp.cancel(requests.get(1));
//        chocHttp.cancel(requests.get(3));
//        chocHttp.cancel(requests.get(5));
//        chocHttp.cancel(request);
    }

    public static class POJO {
        @SerializedName("key0")
        public String key0;
    }

    public static class POJO1 {
        public String key0;
    }


}
