package com.ytempest.tinyimgplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class KeyAction extends AnAction {

    public KeyAction() {
        super("Edit Key", "edit your key for TinyPNG", null);
    }

    int a = 1;

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        ConfigHelper.getInstance().editKey(project);
        System.out.println("a=" + a);
        getTemplatePresentation().setText("" + (a++));
    }
}
