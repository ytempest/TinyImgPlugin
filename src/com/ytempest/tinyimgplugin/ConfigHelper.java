package com.ytempest.tinyimgplugin;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;

import org.apache.http.util.TextUtils;

/**
 * @author heqidu
 * @since 2020/1/20
 */
public class ConfigHelper {
    private volatile static ConfigHelper sInstance = null;

    public static ConfigHelper getInstance() {
        if (sInstance == null) {
            synchronized (ConfigHelper.class) {
                if (sInstance == null) {
                    sInstance = new ConfigHelper();
                }
            }
        }
        return sInstance;
    }

    private ConfigHelper() {
    }

    private static final String API_KEY = "api_key";

    public String getKey() {
        //获取 application 级别的 PropertiesComponent
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getValue(API_KEY, "");
    }

    public String editKey(Project project) {
        String key = getKey();
        key = Messages.showInputDialog(project, "Input your API_KEY", "TinyImg", Messages.getInformationIcon(),
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

        if (!TextUtils.isEmpty(key)) {
            PropertiesComponent properties = PropertiesComponent.getInstance();
            properties.setValue(API_KEY, key);
        }

        return getKey();
    }

    private static final String WINDOW_STATUS = "window_status";

    public void setWindowEnable(boolean enable) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue(WINDOW_STATUS, String.valueOf(enable));
    }

    public boolean isWindowEnable() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return Boolean.valueOf(properties.getValue(WINDOW_STATUS, "true"));
    }
}
