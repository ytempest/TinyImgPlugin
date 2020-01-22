package com.ytempest.tinyimgplugin.action;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author heqidu
 * @since 2020/1/21
 */
public class TinyImgGroup extends ActionGroup {

    /**
     * 如果这里不设置静态属性，那么每次使用到Action时会都会创建新的
     * 如想每一个Action的执行时都是一个新的状态，那么可以不设置为静态属性
     */
    private static final AnAction[] AN_ACTIONS = {
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
