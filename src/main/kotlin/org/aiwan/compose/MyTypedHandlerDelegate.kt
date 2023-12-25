package org.aiwan.compose

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.popup.list.ListPopupImpl
import org.jetbrains.kotlin.idea.KotlinLanguage

class MyTypedHandlerDelegate : TypedHandlerDelegate() {
    private val key = ','
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (c == key) {
            showPopup(project, editor, file)
            return Result.CONTINUE
        }
        return Result.CONTINUE
    }

    private fun showPopup(project: Project, editor: Editor, file: PsiFile) {
        if (file.language !is KotlinLanguage) {
            return
        }
        var element = file.findElementAt(editor.caretModel.offset)
        if (element != null) {
            element = PsiTreeUtil.findFirstParent(element) { it is PsiComment }
        }
        if (element is PsiComment) {
            createPopup(project, listOf("test1", "test2", "test3")) {
                WriteCommandAction.runWriteCommandAction(project) {
                    editor.document.deleteString(
                        editor.caretModel.offset - key.toString().length,
                        editor.caretModel.offset
                    )
                    editor.document.insertString(editor.caretModel.offset, it)
                    editor.caretModel.moveToOffset(editor.caretModel.offset + it.length)
                }
            }.showInBestPositionFor(editor)
        }
    }

    private fun createPopup(project: Project, actions: List<String>, invokeAction: (String) -> Unit): ListPopup {
        val step = object : BaseListPopupStep<String>(null, actions) {
            override fun getTextFor(action: String) = action
            override fun onChosen(selectedValue: String, finalChoice: Boolean): PopupStep<*>? {
                invokeAction(selectedValue)
                return FINAL_CHOICE
            }
        }
        return ListPopupImpl(project, step)
    }
}