package com.android.liuzhuang.chochttplibrary;

import android.app.Application;

import com.android.liuzhuang.chochttplibrary.core.AsyncCall;
import com.android.liuzhuang.chochttplibrary.core.ChocConfig;
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
    private ChocConfig config;

    private ChocHttp(Builder builder) {
        if (ContextHolder.getApplication() == null) {
            throw new NullPointerException("You Must Init ChocHttp First!");
        }
        this.converterFactory = builder.converterFactory;
        this.config = builder.config;
        if (this.config == null) {
            this.config = new ChocConfig();
        }
    }

    public void asyncRequest(BaseRequest request) {
        asyncRequest(request, null, null);
    }

    public void asyncRequest(BaseRequest request, ChocHttpListener listener) {
        asyncRequest(request, listener, null);
    }

    public void asyncRequest(BaseRequest request, ChocHttpListener listener, Class clazz) {
        if (request == null) {
            throw new NullPointerException("request cannot be null!");
        }
        AsyncCall call = new AsyncCall(request, listener, clazz);
        call.setConverterFactory(converterFactory);
        call.setConfig(config);
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
        public ChocConfig config;

        public Builder setConverterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder setConfig(ChocConfig config) {
            this.config = config;
            return this;
        }

        public ChocHttp build() {
            return new ChocHttp(this);
        }
    }
}
