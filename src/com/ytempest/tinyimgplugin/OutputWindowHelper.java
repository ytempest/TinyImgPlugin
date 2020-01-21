package com.ytempest.tinyimgplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class OutputWindowHelper {
    private volatile static OutputWindowHelper sInstance = null;
    private JScrollBar mBar;
    private Project mProject;

    public static OutputWindowHelper getInstance() {
        if (sInstance == null) {
            synchronized (OutputWindowHelper.class) {
                if (sInstance == null) {
                    sInstance = new OutputWindowHelper();
                }
            }
        }
        return sInstance;
    }

    private OutputWindowHelper() {
    }

    public static final String WINDOW_NAME = "TinyImg";

    private ToolWindow toolWindow;
    private JTextArea mOutputArea;

    public boolean init(Project project) {
        if (toolWindow != null) {
            return true;
        }
        toolWindow = ToolWindowManager.getInstance(project).getToolWindow(WINDOW_NAME);
        if (toolWindow == null) {
            showErrMsg(String.format("Can'nt found the %s window", WINDOW_NAME));
            return false;
        }

        mProject = project;
        toolWindow.setAvailable(true, () -> System.out.println(String.format("Set %s window available", WINDOW_NAME)));
        try {
            // ToolWindow未初始化时
            Content tinyimgContent = toolWindow.getContentManager().getContent(0);
            System.out.println("tinyimgContent = " + tinyimgContent);
            JPanel mainPanel = (JPanel) tinyimgContent.getComponent().getComponent(0);
            System.out.println("mainPanel = " + mainPanel);
            JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(0);
            mBar = scrollPane.getVerticalScrollBar();
            System.out.println("scrollPane = " + scrollPane);
            mOutputArea = (JTextArea) scrollPane.getViewport().getComponent(0);
        } catch (Exception e) {
            showErrMsg(String.format("Fail to get view of %s window", WINDOW_NAME));
            return false;
        }
        return true;
    }

    private void showErrMsg(String msg) {
        Messages.showMessageDialog(msg, "Error!!!", Messages.getWarningIcon());
    }


    public void setWindowEnable(Project project, boolean enable) {
        if (init(project)) {
            toolWindow.setAvailable(enable, () -> System.out.println(String.format("Set enable %s window : %s", WINDOW_NAME, enable)));
        }
    }

    public void show() {
        toolWindow.setAvailable(true, null);
        toolWindow.show(() -> System.out.println(String.format("Show %s window", WINDOW_NAME)));
    }

    public void print(String msg) {
        ToolWindowManager.getInstance(mProject).invokeLater(new Runnable() {
            @Override
            public void run() {
                mOutputArea.append(msg + "\n");
            }
        });
    }
}