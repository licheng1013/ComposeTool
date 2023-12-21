package org.aiwan.compose

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import java.awt.event.MouseEvent
import kotlin.io.path.Path

class MyGutterIconNavigationHandler(private val path:String) : GutterIconNavigationHandler<PsiElement> {

    override fun navigate(e: MouseEvent, elt: PsiElement) {
        val file = VirtualFileManager.getInstance().findFileByNioPath(Path(path))
        if (file != null) {
            OpenFileDescriptor(elt.project, file).navigate(true)
        }
    }
}
