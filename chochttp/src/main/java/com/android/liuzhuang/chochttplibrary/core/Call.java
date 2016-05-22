package com.android.liuzhuang.chochttplibrary.core;

import com.android.liuzhuang.chochttplibrary.ChocHttpListener;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;

import java.net.MalformedURLException;

/**
 * The task wrapper.
 * Created by liuzhuang on 16/3/29.
 */
public abstract class Call implements Runnable {
    protected BaseRequest request;
    protected ChocHttpListener callback;
    protected Converter.Factory converterFactory;
    protected ChocConfig config;

    public Call(BaseRequest request, ChocHttpListener callback) {
        this.request = request;
        this.callback = callback;
    }

    public BaseRequest getRequest() {
        return request;
    }

    public void setConverterFactory(Converter.Factory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public void setConfig(ChocConfig config) {
        this.config = config;
    }

    public abstract void cancel();
}
