package com.recursive.quester.framework.requirements

import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox

class DialogRequirement : AbstractRequirement {
    var talkerName: String? = null
    private val text: MutableList<String> = mutableListOf()
    private val mustBeActive: Boolean
    private var hasSeenDialog = false

    constructor(vararg text: String) {
        this.text.addAll(text)
        this.mustBeActive = false
    }

    constructor(talkerName: String?, text: String, mustBeActive: Boolean) {
        this.talkerName = talkerName
        this.text.add(text)
        this.mustBeActive = mustBeActive
    }

    constructor(talkerName: String?, mustBeActive: Boolean, vararg text: String) {
        this.talkerName = talkerName
        this.text.addAll(text)
        this.mustBeActive = mustBeActive
    }

    constructor(text: String) : this(null, text, false)

    override fun check(): Boolean {
        return hasSeenDialog
    }

    fun validateCondition(chatMessage: Chatbox.Message) {
        if (chatMessage.type != Chatbox.Message.Type.SERVER) return
        val dialogMessage = chatMessage.message

        if (!hasSeenDialog) {
            hasSeenDialog = isCurrentDialogMatching(dialogMessage)
        } else if (mustBeActive) {
            hasSeenDialog = isCurrentDialogMatching(dialogMessage)
        }
    }

    private fun isCurrentDialogMatching(dialogMessage: String): Boolean {
        if (talkerName != null && !dialogMessage.contains("$talkerName|")) return false
        return text.any { dialogMessage.contains(it) }
    }

    override fun getDisplayText(): String {
        return "Dialog: ${text.joinToString(", ")}"
    }
}