package com.ytempest.tinyimgplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class Utils {

    public static String showEditKeyDialog(Project project, String key) {
        return Messages.showInputDialog(project, "Input your API_KEY", "Edit Key", Messages.getInformationIcon(),
                key, new InputValidator() {
                    @Override
                    public boolean checkInput(String s) {
                        return s.length() > 0;
                    }

                    @Override
                    public boolean canClose(String s) {
                        return s.length() > 0;
                    }
                }, new TextRange(key.length(), key.length()));
    }
}
