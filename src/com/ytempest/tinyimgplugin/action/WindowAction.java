package com.ytempest.tinyimgplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.ytempest.tinyimgplugin.ConfigHelper;
import com.ytempest.tinyimgplugin.TextWindowHelper;

/**
 * @author heqidu
 * @since 2020/1/21
 */
public class WindowAction extends AnAction {

    private static final String HIDE = "hide window";
    private static final String SHOW = "show window";
    private static final String DESCRIPTION = "set the window status";


    public WindowAction() {
        getTemplatePresentation().setText(getName());
        getTemplatePresentation().setDescription(DESCRIPTION);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        boolean enable = !ConfigHelper.getInstance().isWindowEnable();
        TextWindowHelper.getInstance().setWindowEnable(enable);
        anActionEvent.getPresentation().setText(getName());
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        e.getPresentation().setText(getName());
    }

    private String getName() {
        boolean enable = ConfigHelper.getInstance().isWindowEnable();
        return enable ? HIDE : SHOW;
    }
}
