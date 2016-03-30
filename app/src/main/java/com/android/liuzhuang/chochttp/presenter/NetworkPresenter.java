package com.android.liuzhuang.chochttp.presenter;

import android.util.Log;

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

    public interface NetworkService {
        @GET("users/{user}/repos")
        Call<ResponseBody> listRepos(@Path("user") String user);
    }

    public static interface NetworkCallback{
        void onSuccess(String response);
        void onError(String error);
    }
}
