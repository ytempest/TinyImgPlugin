package com.ytempest.tinyimgplugin.tiny;

import com.intellij.openapi.vfs.VirtualFile;
import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.Result;
import com.tinify.ServerException;
import com.tinify.Source;
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
 * @since 2020/1/13
 */
public class TinyHelper {

    private volatile static TinyHelper sInstance = null;

    public static TinyHelper getInstance() {
        if (sInstance == null) {
            synchronized (TinyHelper.class) {
                if (sInstance == null) {
                    sInstance = new TinyHelper();
                }
            }
        }
        return sInstance;
    }

    private TinyHelper() {
    }

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ExecutorService getExecutor() {
        return executor;
    }

    public static void setKey(String key) {
        Tinify.setKey(key);
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
            OutUtils.d("upload and compress : " + srcFilePath);
            long beforeSize = new File(srcFilePath).length();
//            Source source = Tinify.fromFile(srcFilePath);
//            Result result = source.result();

            Thread.sleep(new Random().nextInt(3000) + 3000);

            OutUtils.d("download to : " + srcFilePath);
//            long afterSize = result.size();
//            result.toFile(tarFilePath);

            OutUtils.d(String.format("finish compress : %s, size: %skb -> %skb", srcFilePath, beforeSize, 11));
        } catch (Exception e) {
            OutUtils.e("Fail to compress : " + srcFilePath);
            OutUtils.e("The error message is: " + e.getMessage());

            if (e instanceof AccountException) {
                OutUtils.e("Please verify your API key and account limit");

            } else if (e instanceof ClientException) {
                OutUtils.e("Please check your source image and request options");

            } else if (e instanceof ServerException) {
                OutUtils.e("Temporary issue with the Tinify API");

            } else if (e instanceof ConnectionException) {
                OutUtils.e("A network connection error occurred, please try again");

            } else {
                OutUtils.e("Something else went wrong, unrelated to the Tinify API");
            }
        }
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
        OutUtils.d("===============start compress===============");
        OutUtils.d("Use key : " + Tinify.key());

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

            OutUtils.d("Already used count : " + Tinify.compressionCount());
            OutUtils.d("===============finish compress===============");
        } catch (Exception e) {
            OutUtils.e("unknown error : " + e.getMessage());
        }
    }
}
