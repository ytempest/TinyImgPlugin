package com.ytempest.tinyimgplugin;

import com.intellij.openapi.components.ServiceManager;

/**
 * @author heqidu
 * @since 2020/1/21
 */
public interface servie {
    static servie getInstance() {
        return ServiceManager.getService(servie.class);
    }
}
