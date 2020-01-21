package com.ytempest.tinyimgplugin.group;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.ytempest.tinyimgplugin.KeyAction;
import com.ytempest.tinyimgplugin.WindowAction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author heqidu
 * @since 2020/1/21
 */
public class TinyImgGroup extends ActionGroup {

    public static final AnAction[] AN_ACTIONS = {
            new KeyAction(),
            new WindowAction()
    };

    public TinyImgGroup() {
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        return AN_ACTIONS;
    }


}
