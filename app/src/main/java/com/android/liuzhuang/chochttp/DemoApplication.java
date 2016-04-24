package com.android.liuzhuang.chochttp;

import android.app.Application;

import com.android.liuzhuang.chochttplibrary.ChocHttp;

/**
 * Created by liuzhuang on 16/4/21.
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ChocHttp.init(this);
    }
}
