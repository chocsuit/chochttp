package com.android.liuzhuang.chochttplibrary.common;

import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhuang on 16/3/26.
 */
public class Headers {
    private LinkedHashMap<String, List<String>> headers = new LinkedHashMap<String, List<String>>();

    public Headers() {
    }

    public Headers(HashMap<String, List<String>> headers) {
        if (headers == null) {
            throw new NullPointerException("headers can not be null");
        }
        if (!headers.isEmpty()) {
            headers.clear();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                this.headers.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addHeader(String key, List<String> value) {
        if (headers != null && !CheckUtil.isEmpty(key) && !CheckUtil.isEmpty(value)) {
            headers.put(key, value);
        }
    }

    public List<String> getHeader(String key) {
        if (headers != null) {
            return headers.get(key);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
            builder.append(entry.getKey().trim()).append(": ").append(entry.getValue()).append("\r\n");
        }
        return builder.toString();
    }
}
