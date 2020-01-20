package com.ytempest.tinyimgplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.ytempest.tinyimgplugin.tiny.TinyHelper;

import org.apache.http.util.TextUtils;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class CompressImgAction extends AnAction {



    @Override
    public void actionPerformed(AnActionEvent e) {
        OutputWindowHelper.getInstance().register(e.getProject());

        String key = KeyHelper.getInstance().getKey();
        if (TextUtils.isEmpty(key)) {
            KeyHelper.getInstance().showEditDialog(e.getProject());
            if (TextUtils.isEmpty(KeyHelper.getInstance().getKey())) {
                return;
            }
        }

        OutputWindowHelper.getInstance().show();

        TinyHelper.setKey(key);

        // 获取选中的文件集
        VirtualFile[] fileArray = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (fileArray == null) {
            return;
        }
        for (VirtualFile virtualFile : fileArray) {
            TinyHelper.compressFile(virtualFile);
        }
    }
}
