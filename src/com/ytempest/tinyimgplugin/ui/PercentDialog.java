package com.ytempest.tinyimgplugin.ui;


import com.ytempest.tinyimgplugin.util.Size;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class PercentDialog extends JDialog {

    private static final int MAX_VAL = 1000;

    public static PercentDialog newInstance() {
        return newInstance(null);
    }

    /**
     * 如果size不为null，则显示数值变化
     */
    public static PercentDialog newInstance(Size size) {
        PercentDialog dialog = new PercentDialog();
        dialog.setImageSize(size);
        dialog.pack();
        dialog.setLocationRelativeTo(dialog);
        return dialog;
    }

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider mPercentSlider;
    private JLabel mPercentLabel;
    private JLabel mTitleLabel;
    private JTextField mSrcWidth, mSrcHeight;
    private JTextField mDestWidth, mDestHeight;
    private JPanel mSizePanel;
    private Size mSrcSize;

    public PercentDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("缩放图片");
        mTitleLabel.setText("请选择缩放比例");
        mSizePanel.setVisible(false);

        mPercentSlider.setMinimum(1);
        mPercentSlider.setMaximum(MAX_VAL);
        mPercentSlider.setValue(MAX_VAL / 2);
        mPercentSlider.addChangeListener(e -> updateScaleVal());
        updateScaleVal();

        buttonOK.addActionListener(e -> {
            dispose();
            mListener.onConfirm(PercentDialog.this);
        });

        buttonCancel.addActionListener(e -> {
            dispose();
            mListener.onCancel(PercentDialog.this);
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                mListener.onCancel(PercentDialog.this);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> {
            dispose();
            mListener.onCancel(PercentDialog.this);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setImageSize(Size size) {
        if (size != null) {
            mSizePanel.setVisible(true);
            mSrcSize = size;
            updateScaleVal();
        }
    }

    public float getScale() {
        return 1F * mPercentSlider.getValue() / MAX_VAL;
    }

    private void updateScaleVal() {
        float scale = getScale();
        if (mSrcSize != null) {
            mSrcWidth.setText(String.valueOf(mSrcSize.width));
            mSrcHeight.setText(String.valueOf(mSrcSize.height));
            mDestWidth.setText(String.valueOf((int) (mSrcSize.width * scale)));
            mDestHeight.setText(String.valueOf((int) (mSrcSize.height * scale)));
        }
        mPercentLabel.setText(String.format("%.1f %%", scale * 100));
    }

    /*onCancel*/

    private onActionListener mListener = new onActionListener() {
        @Override
        public void onConfirm(PercentDialog dialog) {

        }

        @Override
        public void onCancel(PercentDialog dialog) {

        }
    };

    public PercentDialog setOnDismissListener(onActionListener listener) {
        mListener = listener;
        return this;
    }

    public interface onActionListener {

        void onConfirm(PercentDialog dialog);

        void onCancel(PercentDialog dialog);
    }

    public static void main(String[] args) {
        PercentDialog dialog = PercentDialog.newInstance();
        dialog.setImageSize(new Size(1920, 1080));
        dialog.setVisible(true);
        System.exit(0);
    }
}
