package org.aiwan.compose

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtProperty
import java.awt.Image
import java.io.File
import javax.swing.ImageIcon


class MyLineMarkerProvider : RelatedItemLineMarkerProvider() {

    val aa = "/aa/aa.png"

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val file = element.containingFile.virtualFile ?: return
        if (element !is KtProperty) {
            return
        }
        val match = Regex("\"(.*)\"").find(element.text) ?: return
        val path = match.groupValues[1]
        val indexOf = file.path.indexOf("kotlin")
        if (indexOf == -1) {
            return
        }
        // 截断后获取资源目录
        val resourcesPath = file.path.subSequence(0, indexOf).toString() + "resources" + path
        val resourcesFile  = File(resourcesPath)
        if(!resourcesFile.exists()){
            return
        }
        val fileName = resourcesFile.name
        var isImage = false
        for (it in fileSuffix) {
            if (fileName.endsWith(it)) {
                isImage = true
                break
            }
        }
        if(!isImage){
            return
        }

        val type = PsiTreeUtil.findChildOfType(element, LeafPsiElement::class.java)
        if (type != null) {
            val builder = iconBuilder(resourcesPath)
            result.add(builder.createLineMarkerInfo(type,MyGutterIconNavigationHandler(resourcesPath)))
        }
    }

    private val fileSuffix = hashSetOf(".png", ".jpg", ".jpeg", ".gif", ".bmp")

    private fun iconBuilder(path: String): NavigationGutterIconBuilder<PsiElement> {
        val originalIcon = ImageIcon(path)
        val originalImage: Image = originalIcon.image
        val resizedImage: Image = originalImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH) // 设置新的宽度和高度
        val resizedIcon = ImageIcon(resizedImage)

        return NavigationGutterIconBuilder.create(resizedIcon)
            .setAlignment(GutterIconRenderer.Alignment.LEFT).setTarget(null)
    }
}