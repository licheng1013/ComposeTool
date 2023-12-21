package org.aiwan.compose

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtStringTemplateExpression


class MyLineMarkerProvider : RelatedItemLineMarkerProvider() {


    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element !is KtStringTemplateExpression) {
            return
        }
        ResUtil(element, Platform.Compose, result);
    }

}