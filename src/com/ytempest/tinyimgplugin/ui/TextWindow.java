package com.ytempest.tinyimgplugin.ui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2020/4/8
 */
public interface TextWindow {

    String WINDOW_NAME = "TinyImg";

    @Retention(RetentionPolicy.SOURCE)
    @interface TabIndex {
        int COMPRESS_IMG = 0;
        int SCALE_IMG = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface TabName {
        String COMPRESS_IMG = "compressImg";
        String SCALE_IMG = "scaleImg";
    }
}
