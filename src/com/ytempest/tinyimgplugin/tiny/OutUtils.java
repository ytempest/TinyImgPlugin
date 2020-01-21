package com.ytempest.tinyimgplugin.tiny;

import com.ytempest.tinyimgplugin.OutputWindowHelper;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class OutUtils {

    public static void d(String msg) {
        System.out.println(msg);
        OutputWindowHelper.getInstance().print(msg);
    }

    public static void e(String msg) {
        System.err.println();
        OutputWindowHelper.getInstance().print(msg);
    }
}
