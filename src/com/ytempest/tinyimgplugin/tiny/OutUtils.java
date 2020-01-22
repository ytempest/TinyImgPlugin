package com.ytempest.tinyimgplugin.tiny;

import com.intellij.openapi.project.Project;
import com.ytempest.tinyimgplugin.TextWindowHelper;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class OutUtils {

    public static void d(Project project, String msg) {
        System.out.println(msg);
        TextWindowHelper.getInstance().print(msg, project);
    }

    public static void e(Project project, String msg) {
        System.err.println();
        TextWindowHelper.getInstance().print(msg, project);
    }
}
