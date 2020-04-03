package com.ytempest.tinyimgplugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
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

    private static final String WINDOW_NAME = "TinyImg";

    public void setWindowEnable(boolean enable) {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            ToolWindow toolWindow = getToolWindow(project);
            if (toolWindow != null) {
                ConfigHelper.getInstance().setWindowEnable(enable);
                toolWindow.setAvailable(enable, () -> TinyLog.d(TAG, "setWindowEnable: " + WINDOW_NAME + " is enable: " + enable));
            }
        }
    }

    public void show(Project project) {
        ToolWindow toolWindow = getToolWindow(project);
        if (toolWindow != null) {
            setWindowEnable(true);
            toolWindow.show(() -> TinyLog.d(TAG, "Show window : " + WINDOW_NAME));
        }
    }

    public void print(String msg, Project project) {
        ToolWindow toolWindow = getToolWindow(project);
        if (toolWindow == null) {
            return;
        }

        JTextArea outputPanel = getOutputPanel(toolWindow);
        if (outputPanel == null) {
            return;
        }

        ToolWindowManager.getInstance(project).invokeLater(new Runnable() {
            @Override
            public void run() {
                outputPanel.append(msg + "\n");
            }
        });
    }

    @Nullable
    public ToolWindow getToolWindow(Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(WINDOW_NAME);
    }

    @Nullable
    public JTextArea getOutputPanel(@NotNull ToolWindow toolWindow) {
        JTextArea outputPanel = null;
        try {
            // ToolWindow未初始化时
            Content rootContent = toolWindow.getContentManager().getContent(0);
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