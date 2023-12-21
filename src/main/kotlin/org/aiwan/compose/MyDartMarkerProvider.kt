package org.aiwan.compose

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiElement
import com.jetbrains.lang.dart.psi.impl.DartStringLiteralExpressionImpl


class MyDartMarkerProvider : RelatedItemLineMarkerProvider() {


    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element !is DartStringLiteralExpressionImpl) {
            return
        }
        ResUtil(element, Platform.Flutter, result)
    }


}