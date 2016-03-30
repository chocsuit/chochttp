package com.android.liuzhuang.chochttplibrary;

import com.android.liuzhuang.chochttplibrary.request.BaseRequest;

import java.net.MalformedURLException;

/**
 * The task wrapper.
 * Created by liuzhuang on 16/3/29.
 */
public abstract class Call implements Runnable {
    protected BaseRequest request;
    protected IChocHttpCallback callback;

    public Call(BaseRequest request, IChocHttpCallback callback) {
        this.request = request;
        this.callback = callback;
    }

    public BaseRequest getRequest() {
        return request;
    }

    public String getHost() {
        try {
            if (request != null && request.getUrl() != null) {
                return request.getUrl().getHost();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public abstract void cancel();

}
