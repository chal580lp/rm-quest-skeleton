package com.recursive.quester.framework.requirements


abstract class AbstractRequirement : Requirement {
    var _tooltip: String? = null

    private var urlSuffix: String? = null

    private val panelReplacement: Requirement? = null

    protected var shouldCountForFilter: Boolean = false

    abstract override fun check(): Boolean

    fun shouldConsiderForFilter(): Boolean {
        return shouldCountForFilter
    }

    abstract override fun getDisplayText(): String


    fun getTooltip(): String? {
        return _tooltip
    }

    fun setTooltip(tooltip: String?) {
        this._tooltip = tooltip
    }


    fun getUrlSuffix(): String? {
        return urlSuffix
    }

    fun setUrlSuffix(urlSuffix: String?) {
        this.urlSuffix = urlSuffix
    }


    fun appendToTooltip(text: String?) {
        val builder = StringBuilder()
        val currentTooltip = getTooltip()
        if (currentTooltip != null) {
            builder.append(currentTooltip)
            builder.append(if (currentTooltip.isEmpty()) "" else "\n")
        }
        if (text != null) {
            builder.append(text)
        }
        this._tooltip = builder.toString()
    }


}