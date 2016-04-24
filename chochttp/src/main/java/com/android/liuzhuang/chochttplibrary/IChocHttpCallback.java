package com.android.liuzhuang.chochttplibrary;

import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;

/**
 * If you want to have feedback of the request, implement this interface and inject to {@link ChocHttp}
 * Created by liuzhuang on 16/3/29.
 */
public interface IChocHttpCallback<T> {
    void onSuccess(BaseResponse rawResponse, T pojoResponse);
    void onError(int statusCode, String errorMessage);
    void onCanceled(BaseRequest request);
}
