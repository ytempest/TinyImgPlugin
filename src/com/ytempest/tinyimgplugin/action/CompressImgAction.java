package com.ytempest.tinyimgplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.ytempest.tinyimgplugin.ConfigHelper;
import com.ytempest.tinyimgplugin.TextWindowHelper;
import com.ytempest.tinyimgplugin.tiny.CompressTask;

import org.apache.http.util.TextUtils;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class CompressImgAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {
        String key = ConfigHelper.getInstance().getKey();
        if (TextUtils.isEmpty(key)) {
            key = ConfigHelper.getInstance().editKey(event.getProject());
            if (TextUtils.isEmpty(key)) {
                return;
            }
        }

        TextWindowHelper.getInstance().show(event.getProject());

        // 获取选中的文件集
        VirtualFile[] fileArray = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (fileArray == null) {
            return;
        }
        for (VirtualFile virtualFile : fileArray) {
            new CompressTask(event.getProject())
                    .key(key)
                    .exe(virtualFile);
        }
    }
}
