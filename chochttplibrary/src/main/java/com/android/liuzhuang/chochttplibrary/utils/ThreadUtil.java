package com.android.liuzhuang.chochttplibrary.utils;

import java.util.concurrent.ThreadFactory;

/**
 * Created by liuzhuang on 16/3/29.
 */
public class ThreadUtil {
    public static ThreadFactory getThreadFactory(final String name, final boolean isDaemon) {
        return new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, name);
                thread.setDaemon(isDaemon);
                return thread;
            }
        };
    }
}
