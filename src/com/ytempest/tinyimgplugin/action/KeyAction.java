package com.ytempest.tinyimgplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.ytempest.tinyimgplugin.ConfigHelper;
import com.ytempest.tinyimgplugin.Utils;

import org.apache.http.util.TextUtils;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class KeyAction extends AnAction {

    private static final String NAME = "Edit Key";
    private static final String DESCRIPTION = "edit your key for TinyPNG";

    public KeyAction() {
        super(NAME, DESCRIPTION, null);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        String originalKey = ConfigHelper.getInstance().getKey();
        String key = Utils.showEditKeyDialog(project, originalKey);
        if (!TextUtils.isEmpty(key)) {
            ConfigHelper.getInstance().setKey(key);
        }
    }
}
