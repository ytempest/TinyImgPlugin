package com.ytempest.tinyimgplugin.util;

/**
 * @author heqidu
 * @since 2020/4/3
 */
public class TinyLog {

    private static final String PREFIX = "【TinyImg】";

    private static final boolean DEBUG = true;

    public static void d(String tag, String msg) {
        if (!DEBUG) {
            System.out.println(String.format("%s %s, %s", PREFIX, tag, msg));
        }
    }

    public static void e(String tag, String msg) {
        if (!DEBUG) {
            System.err.println(String.format("%s %s, %s", PREFIX, tag, msg));
        }
    }
}
