package com.ytempest.tinyimgplugin.tiny;

import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class Utils {

    public static int getSize(Object[] objects) {
        return objects != null ? objects.length : 0;
    }

    public static int getSize(Collection collections) {
        return collections != null ? collections.size() : 0;
    }

    @Nullable
    public static String get(String[] strings, int index) {
        if (strings != null && strings.length > index) {
            return strings[index];
        }
        return null;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
