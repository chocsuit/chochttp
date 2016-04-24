package com.android.liuzhuang.chochttplibrary;

import android.app.Application;

import com.android.liuzhuang.chochttplibrary.core.AsyncCall;
import com.android.liuzhuang.chochttplibrary.core.Dispatcher;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;

/**
 * The face of ChocHttp. T is the output POJO.
 * Created by liuzhuang on 16/3/29.
 */
public class ChocHttp<T> {

    public static void init(Application application) {
        ContextHolder.init(application);
    }

    private ChocHttp(Builder builder) {
        if (ContextHolder.getApplication() == null) {
            throw new NullPointerException("You Must Init ChocHttp First!");
        }
    }

    public void asyncRequest(BaseRequest request) {
        asyncRequest(request, null, null);
    }

    public void asyncRequest(BaseRequest request, IChocHttpCallback<T> callback) {
        asyncRequest(request, callback, null);
    }

    public void asyncRequest(BaseRequest request, IChocHttpCallback<T> callback, Class<T> clazz) {
        if (request == null) {
            throw new NullPointerException("request can not be null!");
        }
        AsyncCall<T> call = new AsyncCall<T>(request, callback, clazz);
        Dispatcher.getInstance().dispatch(call);
    }

    public void cancel(BaseRequest request) {
        Dispatcher.getInstance().cancel(request);
    }

    public void cancelAll() {
        Dispatcher.getInstance().cancelAll();
    }

    public static class Builder<M> {
        public boolean enableRedirect;

        public Builder setEnableRedirect(boolean enableRedirect) {
            this.enableRedirect = enableRedirect;
            return this;
        }

        public ChocHttp<M> build() {
            return new ChocHttp<M>(this);
        }
    }
}
