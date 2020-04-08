package com.ytempest.tinyimgplugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.ytempest.tinyimgplugin.util.TinyLog;

import org.jetbrains.annotations.NotNull;

/**
 * @author heqidu
 * @since 2020/1/22
 * 窗口工厂，用于创建{@link TextToolWindow}，每一个Project都会创建各自的{@link TextToolWindow}
 */
public class TextToolWindowFactory implements ToolWindowFactory {

    private static final String TAG = TextToolWindowFactory.class.getSimpleName();

    @Override
    public void init(ToolWindow window) {
        TinyLog.d(TAG, "init: init ToolWindow :" + window);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        TextToolWindow compressWindow = new TextToolWindow(toolWindow);
        Content compressContent = contentFactory.createContent(compressWindow.getContent(), TextWindow.TabName.COMPRESS_IMG, false);
        toolWindow.getContentManager().addContent(compressContent, TextWindow.TabIndex.COMPRESS_IMG);

        TextToolWindow scaleWindow = new TextToolWindow(toolWindow);
        Content scaleContent = contentFactory.createContent(scaleWindow.getContent(), TextWindow.TabName.SCALE_IMG, false);
        toolWindow.getContentManager().addContent(scaleContent, TextWindow.TabIndex.SCALE_IMG);
    }
}
