package com.ytempest.tinyimgplugin.core;

import com.intellij.openapi.project.Project;
import com.ytempest.tinyimgplugin.ui.TextWindowHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heqidu
 * @since 2020/4/3
 */
public abstract class AbsTask<Target> {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    protected ExecutorService getExecutor() {
        return executor;
    }

    private final Project mProject;

    public AbsTask(Project project) {
        mProject = project;
    }

    protected Project getProject() {
        return mProject;
    }

    protected void println(String msg) {
        System.out.println(msg);
        TextWindowHelper.getInstance().print(msg, mProject);
    }

    public void exe(Target target) {
        executor.execute(() -> onExecute(target));
    }

    protected abstract void onExecute(Target target);
}
