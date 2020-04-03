package com.ytempest.tinyimgplugin.core;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.Result;
import com.tinify.ServerException;
import com.tinify.Source;
import com.tinify.Tinify;
import com.ytempest.tinyimgplugin.ui.TextWindowHelper;
import com.ytempest.tinyimgplugin.util.FileUtils;

import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heqidu
 * @since 2020/1/22
 */
public class CompressTask {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private final Project mProject;
    private final String mProjectPath;

    public CompressTask(Project project) {
        mProject = project;
        @SystemIndependent String basePath = project.getBasePath();
        mProjectPath = basePath != null ? basePath.replace("/", File.separator) : "";
    }

    public CompressTask key(String key) {
        if (key != null && !key.equals(Tinify.key())) {
            Tinify.setKey(key);
        }
        return this;
    }

    public void exe(VirtualFile... files) {
        if (files == null || files.length == 0) {
            return;
        }
        executor.execute(() -> {
            List<VirtualFile> srcFileList = new LinkedList<>();
            for (VirtualFile virtualFile : files) {
                if (virtualFile.isDirectory()) {
                    List<VirtualFile> imageList = FileUtils.listImageFile(virtualFile, false);
                    srcFileList.addAll(imageList);

                } else if (FileUtils.isImageFile(virtualFile)) {
                    srcFileList.add(virtualFile);
                }
            }
            compress(srcFileList);
        });
    }

    /*compress*/

    private final List<String> failList = new ArrayList<>();

    private void compress(List<VirtualFile> inFiles) {
        if (inFiles == null || inFiles.size() == 0) {
            return;
        }

        print("===============start compress===============");
        print("Use key : " + Tinify.key());

        final CountDownLatch latch = new CountDownLatch(inFiles.size());
        for (final VirtualFile srcVirtualFile : inFiles) {
            executor.execute(() -> {
                // 操作临时文件
                File srcFile = new File(srcVirtualFile.getPath());
                File tmpFile = new File(srcVirtualFile.getParent().getPath(), srcVirtualFile.getName() + ".tmp");
                try {
                    print("compress : " + getRelativePath(srcFile.getPath()));
                    compress(srcFile.getPath(), tmpFile.getPath());

                    // 压缩成功后删除重命名临时文件为原文件
                    long beforeSize = srcFile.length();
                    boolean success = srcFile.delete() && tmpFile.renameTo(srcFile);
                    long afterSize = srcFile.length();

                    if (success) {
                        print(String.format("finish compress : %s, size: %skb -> %skb", getRelativePath(srcFile.getPath()), beforeSize, afterSize));
                    } else {
                        failList.add("Fail to process the file : " + tmpFile.getAbsolutePath());
                    }

                } catch (Exception e) {
                    failList.add(e.getMessage());
                }
                latch.countDown();
            });
        }

        try {
            latch.await();

            print("Already used count : " + Tinify.compressionCount());
            print("===============finish compress===============");

            if (failList.size() > 0) {
                print("===============fail list===============");
                for (String msg : failList) {
                    print(msg);
                }
            }
            failList.clear();

        } catch (Exception e) {
            print("unknown error : " + e.getMessage());
        }
        print("");
    }


    /**
     * Use Tinify compress image. There will may be occur some exception.
     * To ensure the success rate of compress, please check the following points.
     * <p>1. Tinify API</p>
     * <p>2. Network</p>
     * <p>
     * About Tinify : <a href>https://tinypng.com/</a>
     *
     * @param srcFilePath  path of image need compress
     * @param destFilePath output path of image compress success
     */
    private void compress(String srcFilePath, String destFilePath) throws Exception {
        try {
            Source source = Tinify.fromFile(srcFilePath);
            Result result = source.result();

            result.toFile(destFilePath);
        } catch (Exception e) {
            String filePath = getRelativePath(srcFilePath);
            String errMsg = e.getMessage();
            if (e instanceof AccountException) {
                errMsg = "Please verify your API key and account limit";

            } else if (e instanceof ClientException) {
                errMsg = "Please check your source image and request options";

            } else if (e instanceof ServerException) {
                errMsg = "Temporary issue with the Tinify API";

            } else if (e instanceof ConnectionException) {
                errMsg = "A network connection error occurred, please try again";
            }
            throw new Exception("Failed image: " + filePath + ", errorMsg: " + errMsg);
        }
    }

    /**
     * 获取指定文件在当前项目的路劲
     */
    private String getRelativePath(String path) {
        return path.replace(mProjectPath + File.separator, "");
    }

    private void print(String msg) {
        System.out.println(msg);
        TextWindowHelper.getInstance().print(msg, mProject);
    }
}
