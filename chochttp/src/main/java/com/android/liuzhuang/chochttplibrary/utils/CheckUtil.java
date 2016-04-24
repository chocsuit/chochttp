package com.android.liuzhuang.chochttplibrary.utils;

import java.util.Collection;

/**
 * Util to check if CharSequence or Collection is empty.
 * Created by liuzhuang on 16/3/25.
 */
public class CheckUtil {
    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Collection str) {
        if (str == null || str.isEmpty())
            return true;
        else
            return false;
    }
}
