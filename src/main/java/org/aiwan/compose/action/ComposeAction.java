package org.aiwan.compose.action;

import com.intellij.ide.fileTemplates.impl.UrlUtil;
import com.intellij.ide.util.scopeChooser.ShowFilesAction;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.thelumiereguy.compose_helper.intention.actions.ComposeBundle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ComposeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 显示文件名对话框
        VirtualFile selectedDirectory = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (selectedDirectory != null && selectedDirectory.isDirectory()) {
            String name = ComposeBundle.message("compose.action.create.file");
            String title = ComposeBundle.message("compose.action.create.file.title");

            String fileName = Messages.showInputDialog(name, title, Messages.getQuestionIcon());
            if (fileName != null) {
                String path = selectedDirectory.getPath();
                String newDirectory = path + File.separator + fileName.toLowerCase();
                // 创建目录
                FileUtil.createDirectory(new File(newDirectory));
                // 截取到 kotlin 之后的路径并转换为包名
                String packageName = newDirectory.substring(newDirectory.indexOf("kotlin") + 7).replace(File.separator, ".");
                packageName = packageName.replace("/", ".");

                String uiFile = newDirectory + File.separator + fileName + ".kt";
                String stateFile = newDirectory + File.separator + fileName + "State.kt";

                String c = compose.replace(ComposeAction.packageName, packageName);
                c = c.replace(ComposeAction.fileName, fileName);

                String s = state.replace(ComposeAction.packageName, packageName);
                s = s.replace(ComposeAction.fileName, fileName);

                // 创建文件
                try {
                    FileUtil.writeToFile(new File(uiFile), c);
                    FileUtil.writeToFile(new File(stateFile), s);
                    // 刷新目录
                    selectedDirectory.refresh(true, true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    static final String compose;
    static final String state;

    static final String packageName = "${package}";
    static final String fileName = "${fileName}";

    static {
        try {
            compose = UrlUtil.loadText(ComposeAction.class.getResource("/templates/compose.txt"));
            state = UrlUtil.loadText(ComposeAction.class.getResource("/templates/state.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        VirtualFile selectedDirectory = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (selectedDirectory != null && selectedDirectory.isDirectory()) {
            e.getPresentation().setVisible(selectedDirectory.getPath().contains("kotlin"));
        } else {
            e.getPresentation().setVisible(false);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }


}
