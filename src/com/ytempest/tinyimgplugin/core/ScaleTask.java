package com.ytempest.tinyimgplugin.core;

import com.intellij.openapi.project.Project;
import com.ytempest.tinyimgplugin.util.DataUtils;
import com.ytempest.tinyimgplugin.util.FileUtils;

import net.coobird.thumbnailator.Thumbnails;

import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author heqidu
 * @since 2020/1/22
 */
public class ScaleTask extends AbsTask<List<File>> {

    private final String mProjectPath;
    private float mScaleVal;

    public ScaleTask(Project project) {
        super(project);
        @SystemIndependent String basePath = project.getBasePath();
        mProjectPath = basePath != null ? basePath.replace("/", File.separator) : "";
    }

    public ScaleTask scale(float val) {
        this.mScaleVal = val;
        return this;
    }

    @Override
    protected void onExecute(List<File> files) {
        if (DataUtils.getSize(files) >= 0) {
            getExecutor().execute(() -> scale(files));
        }
    }

    /*scale*/

    private final List<String> failList = new ArrayList<>();

    private void scale(List<File> inFiles) {
        println("===============start scale===============");

        final CountDownLatch latch = new CountDownLatch(inFiles.size());
        for (final File inFile : inFiles) {
            getExecutor().execute(() -> {
                // 操作临时文件
                File srcFile = new File(inFile.getPath());
                File tmpFile = new File(inFile.getParent(), "tmp-" + inFile.getName());
                try {
                    println("scale : " + getRelativePath(srcFile.getPath()));

                    scaleImage(srcFile, tmpFile);

                    // 压缩成功后删除重命名临时文件为原文件
                    long beforeSize = srcFile.length();
                    boolean success = srcFile.delete() && tmpFile.renameTo(srcFile);
                    long afterSize = srcFile.length();

                    if (success) {
                        println(String.format("finish scale : %s, size: %skb -> %skb", getRelativePath(srcFile.getPath()), beforeSize, afterSize));
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
            println("===============finish scale===============");

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

    private void scaleImage(File srcFile, File tmpFile) throws IOException {
        System.out.println("mScaleVal=" + mScaleVal);
        Thumbnails.of(srcFile)
                .scale(0.5F)
                .outputQuality(1)
                .toFile(tmpFile);
    }


    /**
     * 获取指定文件在当前项目的路劲
     */
    private String getRelativePath(String path) {
        return path.replace(mProjectPath + File.separator, "");
    }
}
