package com.ytempest.tinyimgplugin.tiny;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.Result;
import com.tinify.ServerException;
import com.tinify.Source;
import com.tinify.Tinify;
import com.ytempest.tinyimgplugin.TextWindowHelper;

import java.io.File;
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
        mProjectPath = project.getBasePath();
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

    private void compress(List<VirtualFile> inFiles) {
        if (inFiles == null || inFiles.size() == 0) {
            return;
        }

        print("===============start compress===============");
        print("Use key : " + Tinify.key());

        final CountDownLatch latch = new CountDownLatch(inFiles.size());
        for (final VirtualFile inFile : inFiles) {
            executor.execute(() -> {
                compress(inFile.getPath(), inFile.getPath());
                latch.countDown();
            });
        }

        try {
            latch.await();

            print("Already used count : " + Tinify.compressionCount());
            print("===============finish compress===============\n");
        } catch (Exception e) {
            print("unknown error : " + e.getMessage());
        }
    }


    /**
     * Use Tinify compress image. There will may be occur some exception.
     * To ensure the success rate of compress, please check the following points.
     * <p>1. Tinify API</p>
     * <p>2. Network</p>
     * <p>
     * About Tinify : <a href>https://tinypng.com/</a>
     *
     * @param srcFilePath path of image need compress
     * @param tarFilePath output path of image compress success
     */
    private void compress(String srcFilePath, String tarFilePath) {
        String relativePath = srcFilePath.replace(mProjectPath + "/", "");
        try {
            print("upload and compress : " + relativePath);
            long beforeSize = new File(srcFilePath).length();
            Source source = Tinify.fromFile(srcFilePath);
            Result result = source.result();

            print("download to : " + relativePath);
            long afterSize = result.size();
            result.toFile(tarFilePath);

            print(String.format("finish compress : %s, size: %skb -> %skb", relativePath, beforeSize, afterSize));
        } catch (Exception e) {
            print("Fail to compress : " + relativePath);
            print("The error message is: " + e.getMessage());

            if (e instanceof AccountException) {
                print("Please verify your API key and account limit");

            } else if (e instanceof ClientException) {
                print("Please check your source image and request options");

            } else if (e instanceof ServerException) {
                print("Temporary issue with the Tinify API");

            } else if (e instanceof ConnectionException) {
                print("A network connection error occurred, please try again");

            } else {
                print("Something else went wrong, unrelated to the Tinify API");
            }
        }
    }

    private void print(String msg) {
        System.out.println(msg);
        TextWindowHelper.getInstance().print(msg, mProject);
    }
}
