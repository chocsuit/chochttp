package com.android.liuzhuang.chochttplibrary.cache;

import android.app.Application;

import com.android.liuzhuang.chochttplibrary.ContextHolder;
import com.android.liuzhuang.chochttplibrary.common.Constant;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.utils.FileUtils;
import com.android.liuzhuang.chochttplibrary.utils.Logger;
import com.google.gson.Gson;

/**
 * Manage the http cache.
 * Created by liuzhuang on 16/4/21.
 */
public final class CacheEngine {

    public static void save2cache(String url, BaseResponse response) {
        if (response == null) {
            return;
        }
        String cacheControl = response.getHeader(Constant.HEADER_CACHE_CONTROL);
        if (cacheControl.equalsIgnoreCase("no-cache") || cacheControl.equalsIgnoreCase("no-store")) {
            Logger.println("no cache!");
        } else {
            Application application = ContextHolder.getApplication();
            if (application != null && !CheckUtil.isEmpty(url)) {
                String path = application.getCacheDir() + "/" + url;
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(response);
                FileUtils.writeFile(path, jsonResponse);
            }
        }
    }

    public static boolean hasExpired(String url) {
        return false;
    }

    public static void updateExpires(String url, final long expires) {

    }

    public static BaseResponse createResponse(String url) {
        Application application = ContextHolder.getApplication();
        if (application != null) {
            String path = application.getCacheDir() + "/" + url;
            if (FileUtils.checkPath(path)) {
                String cacheStr = FileUtils.readFile(path);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(cacheStr, BaseResponse.class);
                if (baseResponse != null) {
                    return baseResponse;
                }
            }
        }
        return null;
    }
}
