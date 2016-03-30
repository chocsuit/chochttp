package com.android.liuzhuang.chochttplibrary.request;

import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.common.MediaType;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liuzhuang on 16/3/29.
 */
public abstract class BaseRequest {
    public abstract String getRawUrl();

    public URL getUrl() throws MalformedURLException {
        if (CheckUtil.isEmpty(getRawUrl())) {
            throw new NullPointerException("URL can not be empty!");
        }
        return new URL(getRawUrl());
    }

    public abstract Method getMethod();

    public abstract String getParams();

    public abstract MediaType getContentType();
}
