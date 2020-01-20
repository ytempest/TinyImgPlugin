package com.ytempest.tinyimgplugin.tiny;

import com.ytempest.tinyimgplugin.OutputWindowHelper;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class OutUtils {

    public static void d(String msg) {
        OutputWindowHelper.getInstance().print(msg);
    }

    public static void e(String msg) {
        OutputWindowHelper.getInstance().print(msg);
    }
}
