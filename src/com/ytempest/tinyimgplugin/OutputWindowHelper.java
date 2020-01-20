package com.ytempest.tinyimgplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class OutputWindowHelper {
    private volatile static OutputWindowHelper sInstance = null;

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

    public void unregister(Project project) {
        toolWindow.setAvailable(false, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void register(Project project) {
        if (toolWindow != null) {
            return;
        }
        toolWindow = ToolWindowManager.getInstance(project).getToolWindow(WINDOW_NAME);
        toolWindow.setAvailable(true, new Runnable() {
            @Override
            public void run() {

            }
        });
        if (toolWindow == null) {
            System.err.println("can not found the TinyImg window");

        } else {
            try {
                // ToolWindow未初始化时
                Content tinyimgContent = toolWindow.getContentManager().getContent(0);
                System.out.println("tinyimgContent = " + tinyimgContent);
                if (tinyimgContent != null) {
                    JPanel mainPanel = (JPanel) tinyimgContent.getComponent().getComponent(0);
                    System.out.println("mainPanel = " + mainPanel);
                    JScrollPane scrollPane = (JScrollPane) mainPanel.getComponent(0);
                    System.out.println("scrollPane = " + scrollPane);
                    mOutputArea = (JTextArea) scrollPane.getViewport().getComponent(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void show() {
        toolWindow.show(() -> System.out.println("Show Window"));
    }

    public void print(String msg) {
        mOutputArea.append(msg);
        mOutputArea.append("\n");
    }
}