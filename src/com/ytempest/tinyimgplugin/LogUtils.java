package com.ytempest.tinyimgplugin;

import java.util.logging.Logger;

/**
 * @author heqidu
 * @since 2020/1/17
 */
public class LogUtils {
    private static Logger logger = Logger.getLogger("TinyImg");

    public static void d(String msg) {
        logger.info(msg);
    }

    public static void e(String msg) {
        logger.warning(msg);
    }

}
