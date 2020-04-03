package com.ytempest.tinyimgplugin.util;

import com.intellij.openapi.vfs.VirtualFile;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class FileUtils {

    public static boolean delete(File file) {
        return file.isFile() && file.delete();
    }

    @NotNull
    public static List<VirtualFile> listImageFile(VirtualFile file) {
        return listImageFile(file, false);
    }

    @NotNull
    public static List<VirtualFile> listImageFile(VirtualFile file, boolean recursive) {
        List<VirtualFile> list = new LinkedList<>();
        if (file.isDirectory()) {
            VirtualFile[] files = file.getChildren();
            for (int i = 0, len = DataUtils.getSize(files); i < len; i++) {
                list.addAll(listImageFile(files[i], recursive));
            }
            return list;
        }

        if (isImageFile(file)) {
            list.addAll(listImageFile(file, recursive));
        }
        return list;
    }


    @NotNull
    public static List<File> listImageFile(File file) {
        return listImageFile(file, false);
    }

    @NotNull
    public static List<File> listImageFile(File file, boolean recursive) {
        List<File> list = new LinkedList<>();
        if (file.isFile()) {
            list.add(file);
            return list;
        }

        File[] files = file.listFiles();
        if (DataUtils.getSize(files) > 0) {
            for (File unknown : files) {
                list.addAll(listImageFile(unknown, recursive));
            }
            return list;
        }
        return list;
    }


    public static boolean isImageFile(File file) {
        return !file.isDirectory() && isImageFile(file.getName());
    }

    public static boolean isImageFile(VirtualFile file) {
        return !file.isDirectory() && isImageFile(file.getName());
    }

    public static boolean isImageFile(String name) {
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".jpe") || name.endsWith(".jfif")
                || name.endsWith(".png");
    }
}
