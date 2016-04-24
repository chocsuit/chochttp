package com.android.liuzhuang.chochttplibrary.request;

import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.common.MediaType;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Base Request.
 * Created by liuzhuang on 16/3/29.
 */
public abstract class BaseRequest {

    // Last-Modified
    public String ifModifiedSince;

    // ETag
    public String ifNoneMatch;

    public abstract String getRawUrl();

    public URL getUrl() throws MalformedURLException {
        if (CheckUtil.isEmpty(getRawUrl())) {
            throw new NullPointerException("URL can not be empty!");
        }
        return new URL(getRawUrl());
    }

    public abstract Method getMethod();

    /**
     * return params in format param1=value1&param2=value2</br>
     * do not work in GET method.
     * @return params in format "param1=value1&param2=value2"
     */
    public abstract String getParams();

    /**
     * @return Content-Type
     */
    public abstract MediaType getContentType();
}
