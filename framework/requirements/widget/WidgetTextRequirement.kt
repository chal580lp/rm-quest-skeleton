package com.recursive.quester.framework.requirements.widget

import com.recursive.quester.framework.requirements.AbstractRequirement
import com.recursive.quester.framework.requirements.util.Utils
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces.getAt

class WidgetTextRequirement : AbstractRequirement {
    var hasPassed: Boolean = false
    val groupId: Int
    private val childId: Int
    private var text: List<String>
    private var childChildId: Int = -1
    private var checkChildren: Boolean = false
    private var min: Int = -1
    private var max: Int = -1
    var _displayText: String = ""

    fun setText(text: List<String>) {
        hasPassed = false
        this.text = text
    }

    fun setText(text: String) {
        setText(listOf(text))
    }

    constructor(componentId: Int, vararg text: String) {
        val pair = Utils.unpackWidget(componentId)
        this.groupId = pair.first
        this.childId = pair.second
        this.text = text.toList()
    }

    constructor(componentId: Int, checkChildren: Boolean, vararg text: String) {
        val pair = Utils.unpackWidget(componentId)
        this.groupId = pair.first
        this.childId = pair.second
        this.checkChildren = checkChildren
        this.text = text.toList()
    }

    constructor(groupId: Int, childId: Int, checkChildren: Boolean, vararg text: String) {
        this.groupId = groupId
        this.childId = childId
        this.checkChildren = checkChildren
        this.text = text.toList()
    }

    constructor(groupId: Int, childId: Int, vararg text: String) {
        this.groupId = groupId
        this.childId = childId
        this.text = text.toList()
    }

    constructor(groupId: Int, childId: Int, childChildId: Int, vararg text: String) {
        this.groupId = groupId
        this.childId = childId
        this.childChildId = childChildId
        this.text = text.toList()
    }

    fun addRange(min: Int, max: Int) {
        this.min = min
        this.max = max
    }

    override fun check(): Boolean {
        return checkWidget()
    }

    fun checkWidget(): Boolean {
        val widget = getAt(groupId, childId)?.let { parentWidget ->
            if (childChildId != -1) parentWidget.getChild(childChildId) else parentWidget
        } ?: return false

        return text.any { textOption ->
            (checkChildren && getChildren(widget, textOption) && widget.isVisible) ||
                    (widget.text?.contains(textOption) == true && widget.isVisible)
        }
    }

    private fun getChildren(parentWidget: InterfaceComponent, textOption: String): Boolean {
        val children = parentWidget.children
        if (children.isEmpty()) return false

        val currentMin = if (max == -1 || min == -1) 0 else min.coerceAtLeast(0)
        val currentMax = if (max == -1 || min == -1) children.size else max.coerceAtMost(children.size)

        for (i in currentMin until currentMax) {
            val currentWidget = parentWidget.getChild(i) ?: continue
            if (true) { // TODO: currentWidget.nestedChildren != null
                if (currentWidget.text?.contains(textOption) == true) return true
            } else {
                if (getChildren(currentWidget, textOption)) return true
            }
        }
        return false
    }

    fun checkWidgetText() {
        hasPassed = hasPassed || checkWidget()
    }

    override fun getDisplayText(): String = _displayText
}