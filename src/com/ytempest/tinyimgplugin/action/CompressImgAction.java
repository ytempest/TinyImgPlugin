package com.ytempest.tinyimgplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.ytempest.tinyimgplugin.ConfigHelper;
import com.ytempest.tinyimgplugin.core.CompressTask;
import com.ytempest.tinyimgplugin.ui.TextWindow;
import com.ytempest.tinyimgplugin.ui.TextWindowHelper;
import com.ytempest.tinyimgplugin.util.FileUtils;
import com.ytempest.tinyimgplugin.util.Utils;

import org.apache.http.util.TextUtils;

import java.io.File;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class CompressImgAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            Messages.showMessageDialog("Can not identify project", "Tip", Messages.getWarningIcon());
            return;
        }

        String key = ConfigHelper.getInstance().getKey();
        if (TextUtils.isEmpty(key)) {
            key = Utils.showEditKeyDialog(project, key);
            if (TextUtils.isEmpty(key)) {
                Messages.showMessageDialog("Only after setting key can be used", "Tip", Messages.getInformationIcon());
                return;

            } else {
                ConfigHelper.getInstance().setKey(key);
            }
        }

        // 获取选中的文件集
        VirtualFile[] fileArray = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (fileArray == null) {
            return;
        }

        // 过滤照片文件
        List<File> srcFileList = FileUtils.getImageList(fileArray);

        // 展示输出框
        TextWindowHelper.getInstance().show(project, TextWindow.TabIndex.COMPRESS_IMG);
        // 启动压缩任务
        new CompressTask(project)
                .key(key)
                .exe(srcFileList);
    }
}
