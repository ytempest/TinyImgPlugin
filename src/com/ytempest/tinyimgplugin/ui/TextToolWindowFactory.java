package com.ytempest.tinyimgplugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import org.jetbrains.annotations.NotNull;

/**
 * @author heqidu
 * @since 2020/1/22
 * 窗口工厂，用于创建{@link TextToolWindow}，每一个Project都会创建各自的{@link TextToolWindow}
 */
public class TextToolWindowFactory implements ToolWindowFactory {

    @Override
    public void init(ToolWindow window) {
        System.out.println("init ToolWindow : " + window);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        TextToolWindow textToolWindow = new TextToolWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(textToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
