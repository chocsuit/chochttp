package com.android.liuzhuang.chochttplibrary.core;

import android.os.Handler;
import android.os.Looper;

import com.android.liuzhuang.chochttplibrary.IChocHttpCallback;
import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.google.gson.Gson;


/**
 * The Async Task
 * Created by liuzhuang on 16/3/29.
 */
public class AsyncCall<T> extends Call {

    private boolean hasCancelled = false;

    private ICallFinishListener listener;

    private Class<T> outPOJOClass;

    private Handler handler;

    public AsyncCall(BaseRequest request, IChocHttpCallback callback, Class<T> outPOJOClass) {
        super(request, callback);
        this.outPOJOClass = outPOJOClass;
        handler = new Handler(Looper.getMainLooper());
    }

    public void run() {
        if (!hasCancelled) {
            final BaseResponse response = SimpleHttpEngine.getInstance().sendRequest(request);
            if (!hasCancelled) {
                if (callback != null) {
                    if (CheckUtil.isEmpty(response.getErrorMessage())) {
                        /** callback will be handled on Main Thread*/
                        if (outPOJOClass != null) {
                            Gson gson = new Gson();
                            final T pojo = gson.fromJson(response.getResponseBody(), outPOJOClass);
                            handleOnSuccess(response, pojo);
                        } else {
                            handleOnSuccess(response, null);
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError(response.getStatusCode(), response.getErrorMessage());
                            }
                        });
                    }
                }
                if (listener != null) {
                    listener.onFinish(this);
                }
            }
        }
        if (hasCancelled) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCanceled(request);
                    }
                });
            }
            if (listener != null) {
                listener.onCanceled(this);
            }
        }
    }

    private void handleOnSuccess(final BaseResponse rawResponse, final T pojo) {
        if (callback == null || rawResponse == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(rawResponse, pojo);
            }
        });
    }

    public void setListener(ICallFinishListener listener) {
        this.listener = listener;
    }

    @Override
    public void cancel() {
        this.hasCancelled = true;
    }
}
