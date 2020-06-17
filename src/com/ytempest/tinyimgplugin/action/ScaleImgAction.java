package com.ytempest.tinyimgplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.ytempest.tinyimgplugin.core.ScaleTask;
import com.ytempest.tinyimgplugin.ui.PercentDialog;
import com.ytempest.tinyimgplugin.ui.TextWindow;
import com.ytempest.tinyimgplugin.ui.TextWindowHelper;
import com.ytempest.tinyimgplugin.util.DataUtils;
import com.ytempest.tinyimgplugin.util.FileUtils;
import com.ytempest.tinyimgplugin.util.Size;

import java.io.File;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/4/3
 */
public class ScaleImgAction extends AnAction {

    private static final String TAG = ScaleImgAction.class.getSimpleName();

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            Messages.showMessageDialog("Can not identify project", "Tip", Messages.getWarningIcon());
            return;
        }

        // 获取选中的文件集
        VirtualFile[] fileArray = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (fileArray == null) {
            return;
        }

        // 过滤照片文件
        List<File> srcFileList = FileUtils.getImageList(fileArray);
        PercentDialog dialog;
        if (DataUtils.getSize(srcFileList) == 1) {
            Size imageSize = FileUtils.getImageSize(DataUtils.getFirst(srcFileList));
            dialog = PercentDialog.newInstance(imageSize);
        } else {
            dialog = PercentDialog.newInstance();
        }

        dialog.setOnDismissListener(new PercentDialog.onActionListener() {
            @Override
            public void onConfirm(PercentDialog dialog) {
                startScaleImages(project, srcFileList, dialog.getScale());
            }

            @Override
            public void onCancel(PercentDialog dialog) {
            }
        }).setVisible(true);
    }

    private void startScaleImages(Project project, List<File> srcFileList, float scale) {
        // 展示输出框
        TextWindowHelper.getInstance().show(project, TextWindow.TabIndex.SCALE_IMG);
        // 启动压缩任务
        new ScaleTask(project)
                .scale(scale)
                .exe(srcFileList);
    }
}
