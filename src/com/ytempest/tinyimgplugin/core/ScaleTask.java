package com.ytempest.tinyimgplugin.core;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.ytempest.tinyimgplugin.ui.TextWindow;
import com.ytempest.tinyimgplugin.util.DataUtils;
import com.ytempest.tinyimgplugin.util.FileUtils;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author heqidu
 * @since 2020/1/22
 */
public class ScaleTask extends AbsTask<List<File>> {

    private float mScaleVal;

    public ScaleTask(Project project) {
        super(project, TextWindow.TabIndex.SCALE_IMG);
    }

    public ScaleTask scale(float val) {
        this.mScaleVal = val;
        return this;
    }

    @Override
    protected void onExecute(List<File> files) {
        if (!DataUtils.isEmpty(files)) {
            scale(files);
        }
    }

    /*scale*/

    private final List<String> failList = new LinkedList<>();

    private void scale(List<File> inFiles) {
        println("===============start scale===============");

        println("scale percent: " + mScaleVal);
        final CountDownLatch latch = new CountDownLatch(inFiles.size());
        for (final File inFile : inFiles) {
            getExecutor().execute(() -> {
                // 操作临时文件
                File srcFile = new File(inFile.getPath());
                File tmpFile = new File(inFile.getParent(), "tmp-" + inFile.getName());
                try {
                    println("scale : " + FileUtils.getRelativePath(getProject(), srcFile.getPath()));

                    scaleImage(srcFile, tmpFile);

                    // 压缩成功后删除重命名临时文件为原文件
                    Pair<String, String> srcImg = getImageInfo(srcFile);
                    boolean success = srcFile.delete() && tmpFile.renameTo(srcFile);
                    Pair<String, String> destImg = getImageInfo(srcFile);

                    if (success) {
                        println(String.format("finish scale : %s, resolution: %s -> %s, size: %s -> %s",
                                FileUtils.getRelativePath(getProject(), srcFile.getPath()),
                                srcImg.first, destImg.first, srcImg.second, destImg.second));
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

    private Pair<String, String> getImageInfo(File image) throws IOException {
        BufferedImage srcImg = Thumbnails.of(image).scale(1).asBufferedImage();
        String resolution = srcImg.getWidth() + "x" + srcImg.getHeight();
        String size = FileUtils.convertSize(image.length());
        return new Pair<>(resolution, size);
    }

    private void scaleImage(File srcFile, File tmpFile) throws IOException {
        Thumbnails.of(srcFile)
                .scale(mScaleVal)
                .outputQuality(1)
                .toFile(tmpFile);
    }
}
