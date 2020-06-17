package com.ytempest.tinyimgplugin.util;

import java.util.Collection;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/4/3
 */
public class DataUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static int getSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static int getSize(Object[] array) {
        return array != null ? array.length : 0;
    }

    public static <T> T getFirst(List<T> list) {
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
