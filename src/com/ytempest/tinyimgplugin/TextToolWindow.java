package com.ytempest.tinyimgplugin;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.ui.JBUI;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author heqidu
 * @since 2020/1/19
 */
public class TextToolWindow {

    private ToolWindow mToolWindow;
    private JPanel mRootPanel;
    private JScrollPane mScrollPane;
    private JTextArea mTextArea;

    public TextToolWindow(ToolWindow toolWindow) {
        mToolWindow = toolWindow;

        boolean enable = ConfigHelper.getInstance().isWindowEnable();
        mToolWindow.setAvailable(enable, null);

        init();
    }

    public JPanel getContent() {
        return mRootPanel;
    }

    private void init() {
        mTextArea.setEditable(false);
        mTextArea.setMargin(JBUI.insets(5, 13, 5, 13));
        // 去除边框
        mScrollPane.setBorder(null);

//        // 如需设置透明
//        mScrollPane.setOpaque(false);
//        mScrollPane.getViewport().setOpaque(false); // 一定要这样设置两次
//        mTextArea.setOpaque(false);

        // 鼠标事件
        mTextArea.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent mouseEvent) {
                mTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));   //鼠标进入Text区后变为文本输入指针
            }

            public void mouseExited(MouseEvent mouseEvent) {
                mTextArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   //鼠标离开Text区后恢复默认形态
            }
        });

        // 输入变化事件
//        mTextArea.getCaret().addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mTextArea.getCaret().setVisible(true);   //使Text区的文本光标显示
//            }
//        });
    }
}
