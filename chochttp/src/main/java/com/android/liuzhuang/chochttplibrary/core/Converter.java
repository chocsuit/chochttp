package com.android.liuzhuang.chochttplibrary.core;

import java.lang.reflect.Type;

/**
 * Convert request and response body.
 * Created by liuzhuang on 16/5/15.
 */
public interface Converter<F, T> {
    T convert(F value);

    abstract class Factory {
        public Converter<String, ?> responseBodyConverter(Type type) {
            return null;
        }
    }
}
