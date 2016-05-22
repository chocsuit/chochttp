package com.android.liuzhuang.chochttplibrary.core;

/**
 * Global config of {@link com.android.liuzhuang.chochttplibrary.ChocHttp}
 * Created by liuzhuang on 16/5/22.
 */
public final class ChocConfig {
    /**5 second*/
    public static final int DEFAULT_CONNECT_TIMEOUT = 20 * 1000;
    /**20 second*/
    public static final int DEFAULT_READ_TIMEOUT = 20 * 1000;

    private int connectTimeOut = DEFAULT_CONNECT_TIMEOUT;
    private int readTimeOut = DEFAULT_READ_TIMEOUT;

    private int retryTimes = 3;
    private int retryInterval = 1000;

    /**10M*/
    private int maxDiskCacheSize = 10 * 1024 * 1024;
    /**512K*/
    private int maxMemCacheSize = 512 * 1024;

    private boolean trustAllCertification = true;

    private boolean disableCache = false;

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public int getMaxDiskCacheSize() {
        return maxDiskCacheSize;
    }

    public int getMaxMemCacheSize() {
        return maxMemCacheSize;
    }

    public boolean isTrustAllCertification() {
        return trustAllCertification;
    }

    public boolean isDisableCache() {
        return disableCache;
    }

    public ChocConfig setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public ChocConfig setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public ChocConfig setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public ChocConfig setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
        return this;
    }

    public ChocConfig setMaxDiskCacheSize(int maxDiskCacheSize) {
        this.maxDiskCacheSize = maxDiskCacheSize;
        return this;
    }

    public ChocConfig setMaxMemCacheSize(int maxMemCacheSize) {
        this.maxMemCacheSize = maxMemCacheSize;
        return this;
    }

    public ChocConfig setTrustAllCertification(boolean trustAllCertification) {
        this.trustAllCertification = trustAllCertification;
        return this;
    }

    public ChocConfig setDisableCache(boolean disableCache) {
        this.disableCache = disableCache;
        return this;
    }
}
