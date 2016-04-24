package com.android.liuzhuang.chochttp.presenter;

import android.os.Looper;
import android.util.Log;

import com.android.liuzhuang.chochttplibrary.ChocHttp;
import com.android.liuzhuang.chochttplibrary.IChocHttpCallback;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.request.KeyValueRequest;
import com.android.liuzhuang.chochttplibrary.request.Method;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.Logger;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by liuzhuang on 16/3/24.
 */
public class NetworkPresenter {
//    public static final String url = "http://goodog.top:8888/";

    public void sendByOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpContributors.main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendByRetrofit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .build();
                NetworkService service = retrofit.create(NetworkService.class);
                Call<ResponseBody> res = service.listRepos("helloyingying");
                try {
                    Log.d("response: ", res.execute().body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void sendByChoc() {
        BaseRequest request = new KeyValueRequest.Builder()
                .setUrl("http://http-caching-demo.herokuapp.com/?cache=true")
                .setMethod(Method.GET)
                .build();
        ChocHttp chocHttp = new ChocHttp.Builder().build();
        chocHttp.asyncRequest(request, new IChocHttpCallback() {
            @Override
            public void onSuccess(BaseResponse rawResponse, Object pojoResponse) {
                Logger.println("========Headers=========");
                Logger.println(rawResponse.getHeaders());
                Logger.println("==========Body==========");
                Logger.println(rawResponse.getResponseBody());
            }

            public void onError(int statusCode, String errorMessage) {
                Logger.println(statusCode + "\n" + errorMessage);
            }

            public void onCanceled(BaseRequest request) {
                Logger.println("cancel " + request.getRawUrl() + "  " + request.getParams());
            }
        });
    }

    public interface NetworkService {
        @GET("users/{user}/repos")
        Call<ResponseBody> listRepos(@Path("user") String user);
    }

    public static interface NetworkCallback{
        void onSuccess(String response);
        void onError(String error);
    }
}
