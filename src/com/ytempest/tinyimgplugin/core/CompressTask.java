package com.ytempest.tinyimgplugin.core;

import com.intellij.openapi.project.Project;
import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.Result;
import com.tinify.ServerException;
import com.tinify.Source;
import com.tinify.Tinify;
import com.ytempest.tinyimgplugin.ui.TextWindow;
import com.ytempest.tinyimgplugin.util.DataUtils;
import com.ytempest.tinyimgplugin.util.FileUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author heqidu
 * @since 2020/1/22
 */
public class CompressTask extends AbsTask<List<File>> {

    public CompressTask(Project project) {
        super(project, TextWindow.TabIndex.COMPRESS_IMG);
    }

    public CompressTask key(String key) {
        if (key != null && !key.equals(Tinify.key())) {
            Tinify.setKey(key);
        }
        return this;
    }

    @Override
    protected void onExecute(List<File> files) {
        if (DataUtils.getSize(files) >= 0) {
            compress(files);
        }
    }

    /*compress*/

    private final List<String> failList = new LinkedList<>();

    private void compress(List<File> inFiles) {
        println("===============start compress===============");
        println("Use key : " + Tinify.key());

        final CountDownLatch latch = new CountDownLatch(inFiles.size());
        for (final File inFile : inFiles) {
            getExecutor().execute(() -> {
                // 操作临时文件
                File srcFile = new File(inFile.getPath());
                File tmpFile = new File(inFile.getParent(), inFile.getName() + ".tmp");
                try {
                    println("compress : " + FileUtils.getRelativePath(getProject(), srcFile.getPath()));
                    compress(srcFile.getPath(), tmpFile.getPath());

                    // 压缩成功后删除重命名临时文件为原文件
                    String beforeSize = FileUtils.convertSize(srcFile.length());
                    boolean success = srcFile.delete() && tmpFile.renameTo(srcFile);
                    String afterSize = FileUtils.convertSize(srcFile.length());

                    if (success) {
                        println(String.format("finish compress : %s, size: %s -> %s",
                                FileUtils.getRelativePath(getProject(), srcFile.getPath()), beforeSize, afterSize));
                    } else {
                        failList.add("Fail to process the file : " + tmpFile.getAbsolutePath());
                    }

                } catch (Exception e) {
                    FileUtils.delete(tmpFile);
                    failList.add(e.getMessage());
                }
                latch.countDown();
            });
        }

        try {
            latch.await();

            println("Already used count : " + Tinify.compressionCount());
            println("===============finish compress===============");

            if (failList.size() > 0) {
                println("===============fail list===============");
                for (String msg : failList) {
                    println(msg);
                }
            }
            failList.clear();

        } catch (Exception e) {
            println("unknown error : " + e.getMessage());
        }
        println("");
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
            String filePath = FileUtils.getRelativePath(getProject(), srcFilePath);
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
}
