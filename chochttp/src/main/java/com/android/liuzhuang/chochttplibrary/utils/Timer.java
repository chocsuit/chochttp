package com.android.liuzhuang.chochttplibrary.utils;

/**
 * calculate time during.
 * Created by liuzhuang on 16/4/24.
 */
public class Timer {
    long start;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end(String tag) {
        Logger.println(tag + " during time in millis==>>" + (System.currentTimeMillis() - start));
    }
}
