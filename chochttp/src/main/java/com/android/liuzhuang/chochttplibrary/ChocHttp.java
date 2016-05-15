package com.android.liuzhuang.chochttplibrary;

import android.app.Application;

import com.android.liuzhuang.chochttplibrary.core.AsyncCall;
import com.android.liuzhuang.chochttplibrary.core.Converter;
import com.android.liuzhuang.chochttplibrary.core.Dispatcher;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;

/**
 * The face of ChocHttp. T is the output POJO.
 * Created by liuzhuang on 16/3/29.
 */
public class ChocHttp {
    public static void init(Application application) {
        ContextHolder.init(application);
    }

    private Converter.Factory converterFactory;

    private ChocHttp(Builder builder) {
        if (ContextHolder.getApplication() == null) {
            throw new NullPointerException("You Must Init ChocHttp First!");
        }
        this.converterFactory = builder.converterFactory;
    }

    public void asyncRequest(BaseRequest request) {
        asyncRequest(request, null, null);
    }

    public void asyncRequest(BaseRequest request, IChocHttpCallback callback) {
        asyncRequest(request, callback, null);
    }

    public void asyncRequest(BaseRequest request, IChocHttpCallback callback, Class clazz) {
        if (request == null) {
            throw new NullPointerException("request cannot be null!");
        }
        AsyncCall call = new AsyncCall(request, callback, clazz);
        call.setConverterFactory(converterFactory);
        Dispatcher.getInstance().dispatch(call);
    }

    public void cancel(BaseRequest request) {
        Dispatcher.getInstance().cancel(request);
    }

    public void cancelAll() {
        Dispatcher.getInstance().cancelAll();
    }

    public static class Builder {
        public Converter.Factory converterFactory;

        public Builder setConverterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public ChocHttp build() {
            return new ChocHttp(this);
        }
    }
}
