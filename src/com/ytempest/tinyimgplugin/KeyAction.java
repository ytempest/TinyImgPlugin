package com.ytempest.tinyimgplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class KeyAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        KeyHelper.getInstance().showEditDialog(project);
    }
}
