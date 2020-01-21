package com.ytempest.tinyimgplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.ytempest.tinyimgplugin.tiny.TinyHelper;

import org.apache.http.util.TextUtils;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class CompressImgAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            OutputWindowHelper.getInstance().init(event.getProject());
        } catch (Exception e) {
            Messages.showMessageDialog(e.getMessage(), "Error!!!", Messages.getWarningIcon());
            return;
        }

        String key = ConfigHelper.getInstance().getKey();
        if (TextUtils.isEmpty(key)) {
            key = ConfigHelper.getInstance().editKey(event.getProject());
            if (TextUtils.isEmpty(key)) {
                return;
            }
        }

        OutputWindowHelper.getInstance().show();

        TinyHelper.setKey(key);

        // 获取选中的文件集
        VirtualFile[] fileArray = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (fileArray == null) {
            return;
        }
        for (VirtualFile virtualFile : fileArray) {
            TinyHelper.getInstance().compressFile(virtualFile);
        }
    }
}
