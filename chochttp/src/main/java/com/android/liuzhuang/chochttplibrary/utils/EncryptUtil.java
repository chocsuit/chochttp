package com.android.liuzhuang.chochttplibrary.utils;

/**
 * Created by liuzhuang on 16/3/26.
 */
public class EncryptUtil {
    /** Returns a 32 character string containing an MD5 hash of {@code s}. */
    public static String md5Hex(String s) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(s.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    /** Returns a Base 64-encoded string containing a SHA-1 hash of {@code s}. */
    public static String shaBase64(String s) {
        return "";
    }
}
