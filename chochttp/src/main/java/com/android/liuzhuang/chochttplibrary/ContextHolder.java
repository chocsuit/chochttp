package com.android.liuzhuang.chochttplibrary;

import android.app.Application;

import java.lang.ref.WeakReference;

/**
 * Holder the Application in WeakReference.
 * Created by liuzhuang on 16/4/21.
 */
public final class ContextHolder {
    private static WeakReference<Application> applicationWeakReference;

    public static void init(Application application) {
        applicationWeakReference = new WeakReference<Application>(application);
    }

    public static Application getApplication() {
        if (applicationWeakReference != null && applicationWeakReference.get() != null) {
            return applicationWeakReference.get();
        }
        return null;
    }
}
