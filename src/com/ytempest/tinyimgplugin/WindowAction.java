package com.ytempest.tinyimgplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author heqidu
 * @since 2020/1/21
 */
public class WindowAction extends AnAction {

    public WindowAction() {
        getTemplatePresentation().setText(getName());
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        boolean enable = !ConfigHelper.getInstance().isWindowEnable();
        OutputWindowHelper.getInstance().setWindowEnable(anActionEvent.getProject(), enable);
        ConfigHelper.getInstance().setWindowEnable(enable);
        anActionEvent.getPresentation().setText(getName());
        System.out.println("anActionEvent=" + anActionEvent.getProject());
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        e.getPresentation().setText(getName());
    }

    private String getName() {
        boolean enable = ConfigHelper.getInstance().isWindowEnable();
        return enable ? "hide window" : "show window";
    }
}
