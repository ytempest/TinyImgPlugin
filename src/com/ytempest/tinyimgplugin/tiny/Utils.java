package com.ytempest.tinyimgplugin.tiny;

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
}
