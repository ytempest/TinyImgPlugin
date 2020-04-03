package com.ytempest.tinyimgplugin.util;

import java.util.Collection;

/**
 * @author heqidu
 * @since 2020/4/3
 */
public class DataUtils {
    public static int getSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static int getSize(Object[] array) {
        return array != null ? array.length : 0;
    }
}
