package com.ytempest.tinyimgplugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.sun.istack.internal.NotNull;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class FileUtils {

    /**
     * 获取路径相对于工程目录的路径
     */
    public static String getRelativePath(Project project, String path) {
        String basePath = project.getBasePath();
        if (basePath == null) {
            return path;
        }
        String projectPath = basePath.replace("/", File.separator);
        return path.replace(projectPath + File.separator, "");
    }

    public static boolean delete(File file) {
        return file.isFile() && file.delete();
    }

    /**
     * 获取文件列表下所有一级目录的图片
     */
    public static List<File> getImageList(@NotNull VirtualFile[] fileArray) {
        // 过滤照片文件
        List<File> srcFileList = new ArrayList<>();
        for (VirtualFile virtualFile : fileArray) {
            File file = new File(virtualFile.getPath());
            if (file.isDirectory()) {
                srcFileList.addAll(listImageFile(file));

            } else if (isImageFile(file)) {
                srcFileList.add(file);
            }
        }
        return srcFileList;
    }

    @NotNull
    public static List<File> listImageFile(File file) {
        return listImageFile(file, false);
    }

    @NotNull
    public static List<File> listImageFile(File file, boolean recursive) {
        List<File> list = new LinkedList<>();
        if (file.isFile() && isImageFile(file)) {
            list.add(file);
            return list;
        }

        File[] files = file.listFiles();
        if (!DataUtils.isEmpty(files)) {
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

    public static boolean isImageFile(String name) {
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".jpe") || name.endsWith(".jfif")
                || name.endsWith(".png");
    }


    public static String convertSize(long size) {
        if (size < 1024L) {
            return String.valueOf(size) + "b";
        }

        size = size / 1024L;
        if (size < 1024L) {
            return size + "kb";
        }

        float sizeMB = size / 1024F;
        if (sizeMB < 1024F) {
            return String.format("%.2fM", sizeMB);
        }

        float sizeGB = sizeMB / 1024F;
        return String.format("%.2fG", sizeGB);
    }

    public static Size getImageSize(File image) {
        try {
            if (image != null) {
                BufferedImage srcImg = Thumbnails.of(image).scale(1).asBufferedImage();
                return new Size(srcImg.getWidth(), srcImg.getHeight());
            }
        } catch (IOException e) {
        }
        return new Size(0, 0);
    }
}
