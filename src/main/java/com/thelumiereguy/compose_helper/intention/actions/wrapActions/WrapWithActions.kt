package com.thelumiereguy.compose_helper.intention.actions.wrapActions

import com.intellij.codeInsight.template.impl.TemplateImpl
import com.intellij.codeInsight.template.impl.TemplateSettings
import com.thelumiereguy.compose_helper.intention.actions.ComposeBundle

class WrapWithBoxIntention : BaseWrapWithComposableAction() {

    override fun getText(): String {
        return ComposeBundle.message("wrap.with.box")
    }

    override fun getTemplate(): TemplateImpl? {
        return TemplateSettings.getInstance().getTemplate("boxcomp", "ComposeHelperTemplate")
    }
}

class WrapWithCardIntention : BaseWrapWithComposableAction() {

    override fun getText(): String {
        return ComposeBundle.message("wrap.with.card")
    }

    override fun getTemplate(): TemplateImpl? {
        return TemplateSettings.getInstance().getTemplate("cardcomp", "ComposeHelperTemplate")
    }
}

class WrapWithColumnIntention : BaseWrapWithComposableAction() {

    override fun getText(): String {
        return ComposeBundle.message("wrap.with.column")
    }

    override fun getTemplate(): TemplateImpl? {
        return TemplateSettings.getInstance().getTemplate("columncomp", "ComposeHelperTemplate")
    }
}

class WrapWithRowIntention : BaseWrapWithComposableAction() {

    override fun getText(): String {
        return ComposeBundle.message("wrap.with.row")
    }

    override fun getTemplate(): TemplateImpl? {
        return TemplateSettings.getInstance().getTemplate("rowcomp", "ComposeHelperTemplate")
    }
}

class WrapWithLzyColumnIntention : BaseWrapWithComposableAction() {

    override fun getText(): String {
        return ComposeBundle.message("wrap.with.LazyColumn")
    }

    override fun getTemplate(): TemplateImpl? {
        return TemplateSettings.getInstance().getTemplate("lazycolumncomp", "ComposeHelperTemplate")
    }
}

class WrapWithLzyRowIntention : BaseWrapWithComposableAction() {

    override fun getText(): String {
        return ComposeBundle.message("wrap.with.lazyRow")
    }

    override fun getTemplate(): TemplateImpl? {
        return TemplateSettings.getInstance().getTemplate("lazyrowcomp", "ComposeHelperTemplate")
    }
}