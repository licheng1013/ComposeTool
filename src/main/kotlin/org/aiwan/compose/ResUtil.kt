package org.aiwan.compose

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import java.awt.Image
import java.io.File
import javax.swing.ImageIcon

class ResUtil(
    element: PsiElement,
    private val type: Platform,
    result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
) {
    init {
        stringResource(element, result)
    }

    private fun stringResource(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        val virtualFile = element.containingFile.virtualFile ?: return
        if (element !is KtStringTemplateExpression) {
            return
        }
        val file = getFile(virtualFile, element) ?: return

        if (file.isDirectory || !file.exists()) {
            return
        }

        var isImage = false
        for (it in fileSuffix) {
            if (file.name.endsWith(it)) {
                isImage = true
                break
            }
        }
        val type = PsiTreeUtil.findChildOfType(element, LeafPsiElement::class.java) ?: return
        if (!isImage) {
            result.add(iconOther.createLineMarkerInfo(type, MyGutterIconNavigationHandler(file.absolutePath)))
            return
        }
        val builder = iconImageBuilder(file.absolutePath)
        result.add(builder.createLineMarkerInfo(type, MyGutterIconNavigationHandler((file.absolutePath))))
    }

    private fun getFile(file: VirtualFile, element: PsiElement): File? {
        if (type == Platform.Flutter) {
            return null
        }
        val text = element.text
        // 排除空字符串
        if (text.replace("\"", "").isBlank()) {
            return null
        }
        val match = Regex("\"(.*)\"").find(text) ?: return null
        val path = match.groupValues[1]
        val indexOf = file.path.indexOf("kotlin")
        if (indexOf == -1) {
            return null
        }
        // 截断后获取资源目录
        val resourcesPath = file.path.subSequence(0, indexOf).toString() + "resources" + path
        val resourcesFile = File(resourcesPath)
        if (!resourcesFile.exists()) {
            return null
        }
        return resourcesFile
    }

    companion object {
        private val fileSuffix = hashSetOf(".png", ".jpg", ".jpeg", ".gif", ".bmp")
        private val iconOther = NavigationGutterIconBuilder.create(AllIcons.Actions.ListFiles)
            .setAlignment(GutterIconRenderer.Alignment.LEFT).setTarget(null).setTooltipText("Resource file")

        private fun iconImageBuilder(path: String): NavigationGutterIconBuilder<PsiElement> {
            val originalIcon = ImageIcon(path)
            val originalImage: Image = originalIcon.image
            val resizedImage: Image = originalImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH) // 设置新的宽度和高度
            val resizedIcon = ImageIcon(resizedImage)

            return NavigationGutterIconBuilder.create(resizedIcon)
                .setAlignment(GutterIconRenderer.Alignment.LEFT).setTarget(null).setTooltipText("Resource image")
        }
    }


}
