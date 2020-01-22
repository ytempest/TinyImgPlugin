package com.ytempest.tinyimgplugin.tiny;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.ServerException;
import com.tinify.Tinify;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heqidu
 * @since 2020/1/22
 */
public class CompressTask {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private Project mProject;

    public CompressTask(Project project) {
        mProject = project;
    }

    public CompressTask key(String key) {
        if (key != null && !key.equals(Tinify.key())) {
            Tinify.setKey(key);
        }
        return this;
    }

    public void exe(VirtualFile input) {
        compressFile(input);
    }

    /*compress*/

    public void compressFile(VirtualFile input) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<VirtualFile> files = null;
                if (input.isDirectory()) {
                    files = FileUtils.listImageFile(input, false);

                } else if (FileUtils.isImageFile(input)) {
                    files = new LinkedList<>();
                    files.add(input);
                }

                if (Utils.getSize(files) > 0) {
                    compress(files);
                }
            }
        });
    }

    private void compress(List<VirtualFile> inFiles) {
        OutUtils.d(mProject, "===============start compress===============");
        OutUtils.d(mProject, "Use key : " + Tinify.key());

        final CountDownLatch latch = new CountDownLatch(inFiles.size());
        for (final VirtualFile inFile : inFiles) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    compress(inFile.getPath(), inFile.getPath());
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();

            OutUtils.d(mProject, "Already used count : " + Tinify.compressionCount());
            OutUtils.d(mProject, "===============finish compress===============");
        } catch (Exception e) {
            OutUtils.e(mProject, "unknown error : " + e.getMessage());
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
    public void compress(String srcFilePath, String tarFilePath) {
        try {
            OutUtils.d(mProject, "upload and compress : " + srcFilePath);
            long beforeSize = new File(srcFilePath).length();
//            Source source = Tinify.fromFile(srcFilePath);
//            Result result = source.result();

            Thread.sleep(new Random().nextInt(3000) + 3000);

            OutUtils.d(mProject, "download to : " + srcFilePath);
//            long afterSize = result.size();
//            result.toFile(tarFilePath);

            OutUtils.d(mProject, String.format("finish compress : %s, size: %skb -> %skb", srcFilePath, beforeSize, 11));
        } catch (Exception e) {
            OutUtils.e(mProject, "Fail to compress : " + srcFilePath);
            OutUtils.e(mProject, "The error message is: " + e.getMessage());

            if (e instanceof AccountException) {
                OutUtils.e(mProject, "Please verify your API key and account limit");

            } else if (e instanceof ClientException) {
                OutUtils.e(mProject, "Please check your source image and request options");

            } else if (e instanceof ServerException) {
                OutUtils.e(mProject, "Temporary issue with the Tinify API");

            } else if (e instanceof ConnectionException) {
                OutUtils.e(mProject, "A network connection error occurred, please try again");

            } else {
                OutUtils.e(mProject, "Something else went wrong, unrelated to the Tinify API");
            }
        }
    }

}
