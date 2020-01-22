package com.ytempest.tinyimgplugin;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;

import org.jetbrains.annotations.NotNull;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // 如果不是右击
                if (e.getButton() != MouseEvent.BUTTON3) {
                    return;
                }

                JBPopup popup = getJBPopup();
                // 传入e，获取位置进行显示
                popup.show(new RelativePoint(e));

            }

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

    private static final String CLEAR_ALL = "    Clear All        ";

    @NotNull
    private JBPopup getJBPopup() {
        // 添加右键菜单的内容
        JBList<String> list = new JBList<>();
        // 设置数据
        list.setListData(new String[]{CLEAR_ALL});

        JBPopup popup = new PopupChooserBuilder(list)
                .setItemChoosenCallback(new Runnable() { // 添加点击项的监听事件
                    @Override
                    public void run() {
                        String value = list.getSelectedValue();
                        if (CLEAR_ALL.equals(value)) {
                            mTextArea.setText("");
                        }
                    }
                }).createPopup();


        // 设置大小
        Dimension dimension = popup.getContent().getPreferredSize();
        popup.setSize(new Dimension(dimension.width, dimension.height));
        return popup;
    }
}
