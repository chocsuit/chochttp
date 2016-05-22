package com.android.liuzhuang.chochttplibrary.cache;

import android.app.Application;

import com.android.liuzhuang.chochttplibrary.ContextHolder;
import com.android.liuzhuang.chochttplibrary.common.Constant;
import com.android.liuzhuang.chochttplibrary.core.ChocConfig;
import com.android.liuzhuang.chochttplibrary.response.BaseResponse;
import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;
import com.android.liuzhuang.chochttplibrary.utils.DateUtil;
import com.android.liuzhuang.chochttplibrary.utils.FileUtils;
import com.android.liuzhuang.chochttplibrary.utils.Logger;
import com.android.liuzhuang.chochttplibrary.utils.ThreadUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manage the http cache.
 * Created by liuzhuang on 16/4/21.
 */
public final class CacheEngine {

    private ThreadPoolExecutor executorService;

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), ThreadUtil.getThreadFactory("ChocHttp Dispatcher", false));
        }
        return executorService;
    }


    public synchronized void save2cache(final String url, final BaseResponse response, ChocConfig config) {
        if (response == null || CheckUtil.isEmpty(url) || (config != null && config.isDisableCache())) {
            Logger.println("save to cache error!");
            return;
        }
        executorService().execute(new Runnable() {
            @Override
            public void run() {
                String cacheControl = response.getHeader(Constant.HEADER_CACHE_CONTROL);
                if (!CheckUtil.isEmpty(cacheControl) &&
                        (cacheControl.equalsIgnoreCase("no-cache") || cacheControl.equalsIgnoreCase("no-store"))) {
                    Logger.println("no cache!");
                } else {
                    Application application = ContextHolder.getApplication();
                    if (application != null && !CheckUtil.isEmpty(url)) {
                        try {
                            String path = application.getCacheDir() + "/" + URLEncoder.encode(url);
                            Logger.println("save2cache====>>>" + path);
                            FileUtils.writeFile(path, response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public synchronized BaseResponse createResponse(String url) {
        Application application = ContextHolder.getApplication();
        if (application != null) {
            String path = application.getCacheDir() + "/" + URLEncoder.encode(url);
            Logger.println("createResponse====>>>" + path);
            if (FileUtils.checkPath(path)) {
                try {
                    Object cacheObj = FileUtils.readObjectFromFile(path);
                    if (cacheObj != null && cacheObj instanceof BaseResponse) {
                        return (BaseResponse) cacheObj;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean isExpired(BaseResponse response) {
        if (response == null) {
            return true;
        }
        String expires = response.getHeader(Constant.HEADER_EXPIRES);
        String cacheControl = response.getHeader(Constant.HEADER_CACHE_CONTROL);
        String date = response.getHeader(Constant.HEADER_DATE);
        try {
            // if expires after now
            if (!CheckUtil.isEmpty(expires) && DateUtil.compareToNow(expires) > 0) {
                return false;
            }
            if (!CheckUtil.isEmpty(cacheControl)) {
                String[] cacheControls = cacheControl.split(",");
                String prefix = "max-age=";
                Integer time = 0;
                for (int i = 0; i < cacheControls.length; i++) {
                    String temp = cacheControls[i].trim();
                    if (temp.contains(prefix)) {
                        time = Integer.parseInt(temp.substring(prefix.length()));
                        break;
                    }
                }
                // if max-age is smaller than during time
                if (DateUtil.getDuringMillisFromDate(date) < time * 1000) {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NumberFormatException ne) {
            ne.printStackTrace();
        }
        return true;
    }
}
