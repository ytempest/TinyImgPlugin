package com.ytempest.tinyimgplugin;

import com.intellij.ide.util.PropertiesComponent;

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
        //获取 application 级别的 PropertiesComponent，用于持久化数据
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getValue(API_KEY, "");
    }

    public void setKey(String key) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue(API_KEY, key);
    }

    private static final String WINDOW_STATUS = "window_status";

    public void setWindowEnable(boolean enable) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        // 这里之所以使用String来保存boolean是由于在第一次存储时，若保存的为false，那么这个值是不会
        // 持久化的【神奇的代码】，如果在保存了为false，但是获取时默认为true就会导致：明明已经保存了
        // false，但是获取到的是true
        properties.setValue(WINDOW_STATUS, String.valueOf(enable));
    }

    public boolean isWindowEnable() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return Boolean.valueOf(properties.getValue(WINDOW_STATUS, "true"));
    }
}
