package com.ytempest.tinyimgplugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.ytempest.tinyimgplugin.ConfigHelper;
import com.ytempest.tinyimgplugin.util.TinyLog;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class TextWindowHelper {

    private static final String TAG = TextWindowHelper.class.getSimpleName();

    private volatile static TextWindowHelper sInstance = null;

    public static TextWindowHelper getInstance() {
        if (sInstance == null) {
            synchronized (TextWindowHelper.class) {
                if (sInstance == null) {
                    sInstance = new TextWindowHelper();
                }
            }
        }
        return sInstance;
    }

    private TextWindowHelper() {
    }

    public void setWindowEnable(boolean enable) {
        ConfigHelper.getInstance().setWindowEnable(enable);
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            ToolWindow toolWindow = getToolWindow(project);
            if (toolWindow != null) {
                toolWindow.setAvailable(enable, () -> TinyLog.d(TAG, "setWindowEnable: " + TextWindow.WINDOW_NAME + " is enable: " + enable));
            }
        }
    }

    public void show(Project project, @TextWindow.TabIndex int contentIndex) {
        ToolWindow toolWindow = getToolWindow(project);
        if (toolWindow != null) {
            setWindowEnable(true);
            // 激活窗口
            if (!toolWindow.isActive()) {
                toolWindow.activate(() -> TinyLog.d(TAG, "show: "));
            }
            toolWindow.show(() -> {
                ContentManager manager = toolWindow.getContentManager();
                Content content = manager.getContent(contentIndex);
                if (content != null) {
                    manager.setSelectedContent(content);
                }
                TinyLog.d(TAG, "Show window : " + TextWindow.WINDOW_NAME);
            });
        }
    }

    public void println(Project project, @TextWindow.TabIndex int windowIndex, String msg) {
        ToolWindow toolWindow = getToolWindow(project);
        if (toolWindow == null) {
            return;
        }

        JTextArea outputPanel = getOutputPanel(toolWindow, windowIndex);
        if (outputPanel == null) {
            return;
        }

        ToolWindowManager.getInstance(project).invokeLater(() -> outputPanel.append(msg + "\n"));
    }

    @Nullable
    public ToolWindow getToolWindow(Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(TextWindow.WINDOW_NAME);
    }

    @Nullable
    private JTextArea getOutputPanel(@NotNull ToolWindow toolWindow, @TextWindow.TabIndex int windowIndex) {
        JTextArea outputPanel = null;
        try {
            // ToolWindow未初始化时
            Content rootContent = toolWindow.getContentManager().getContent(windowIndex);
            if (rootContent != null) {
                JPanel mainPanel = (JPanel) rootContent.getComponent().getComponent(0);
                JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(0);
                outputPanel = (JTextArea) scrollPane.getViewport().getComponent(0);
            }
        } catch (Exception e) {
        }
        return outputPanel;
    }
}