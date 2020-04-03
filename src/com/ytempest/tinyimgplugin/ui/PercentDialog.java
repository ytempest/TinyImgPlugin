package com.ytempest.tinyimgplugin.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;

public class PercentDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider mPercentSlider;
    private JLabel mPercentLabel;
    private JLabel mTitleLabel;

    public PercentDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("缩放图片");
        mTitleLabel.setText("请选择缩放比例");
        mTitleLabel.setSize(-1, 130);

        mPercentLabel.setSize(150, -1);

        mPercentSlider.setMinimum(1);
        mPercentSlider.setMaximum(100);
        mPercentSlider.setValue(50);
        mPercentSlider.addChangeListener(e -> updatePercent());
        updatePercent();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                mListener.onConfirm(PercentDialog.this);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                mListener.onCancel(PercentDialog.this);
            }
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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                mListener.onCancel(PercentDialog.this);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public int getPercent() {
        return mPercentSlider.getValue();
    }

    private void updatePercent() {
        mPercentLabel.setText(mPercentSlider.getValue() + "%");
    }

    public static PercentDialog newInstance() {
        PercentDialog dialog = new PercentDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(dialog);
        return dialog;
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
        dialog.setVisible(true);
        System.exit(0);
    }
}
