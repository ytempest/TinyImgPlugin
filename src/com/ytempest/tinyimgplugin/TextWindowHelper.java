package com.ytempest.tinyimgplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.tools.ToolManager;
import com.intellij.ui.content.Content;

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
                toolWindow.setAvailable(enable, () -> System.out.println(String.format("Set enable %s window : %s", WINDOW_NAME, enable)));
            }
        }
    }

    public void show(Project project) {
        ToolWindow toolWindow = getToolWindow(project);
        if (toolWindow != null) {
            setWindowEnable(true);
            toolWindow.show(() -> System.out.println(String.format("Show %s window", WINDOW_NAME)));
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

//        synchronized (outputPanel) {
//
//        }
    }

    @Nullable
    public ToolWindow getToolWindow(Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(WINDOW_NAME);
//        Messages.showMessageDialog("project=" + project + " | toolwindow=" + toolWindow, "title", Messages.getInformationIcon());
        return toolWindow;
    }

    @Nullable
    public JTextArea getOutputPanel(@NotNull ToolWindow toolWindow) {
        JTextArea outputPanel = null;
        try {
            // ToolWindow未初始化时
            Content tinyimgContent = toolWindow.getContentManager().getContent(0);
            if (tinyimgContent != null) {
                JPanel mainPanel = (JPanel) tinyimgContent.getComponent().getComponent(0);
                JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(0);
                outputPanel = (JTextArea) scrollPane.getViewport().getComponent(0);
            }
        } catch (Exception e) {
        }
        System.out.println("outputPanel = " + outputPanel);
        return outputPanel;
    }
}