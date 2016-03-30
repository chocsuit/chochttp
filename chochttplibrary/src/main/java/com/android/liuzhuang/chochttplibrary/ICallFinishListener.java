package com.android.liuzhuang.chochttplibrary;

/**
 * Created by liuzhuang on 16/3/29.
 */
public interface ICallFinishListener {
    void onFinish(AsyncCall call);
    void onCanceled(AsyncCall call);
}
