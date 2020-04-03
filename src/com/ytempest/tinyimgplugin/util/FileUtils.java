package com.ytempest.tinyimgplugin.util;

import com.intellij.openapi.vfs.VirtualFile;
import com.sun.istack.internal.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class FileUtils {

    @NotNull
    public static List<VirtualFile> listImageFile(VirtualFile file) {
        return listImageFile(file, false);
    }

    @NotNull
    public static List<VirtualFile> listImageFile(VirtualFile file, boolean recursive) {
        List<VirtualFile> list = new LinkedList<>();
        if (file != null && file.isDirectory()) {
            VirtualFile[] files = file.getChildren();
//            VfsUtilCore.visitChildrenRecursively();
            int len = files != null ? files.length : 0;
            for (int i = 0; i < len; i++) {
                VirtualFile unknown = files[i];
                if (recursive && unknown.isDirectory()) {
                    list.addAll(listImageFile(unknown, recursive));

                } else if (isImageFile(unknown)) {
                    list.add(unknown);
                }
            }
        }
        return list;
    }


    public static boolean isImageFile(VirtualFile file) {
        if (file.isDirectory()) {
            return false;
        }

        return true;
//        String name = file.getName();
//        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".jpe") || name.endsWith(".jfif")
//                || name.endsWith(".png");
    }
}
