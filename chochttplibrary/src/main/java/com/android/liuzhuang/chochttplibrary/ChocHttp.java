package com.android.liuzhuang.chochttplibrary;

import com.android.liuzhuang.chochttplibrary.request.BaseRequest;

/**
 * The face of ChocHttp
 * Created by liuzhuang on 16/3/29.
 */
public class ChocHttp<T> {
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
}
