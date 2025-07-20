package com.recursive.quester.framework.requirements

import com.recursive.quester.framework.requirements.conditional.ConditionForStep
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox

class ChatMessageRequirement : ConditionForStep {
    var hasReceivedChatMessage: Boolean = false
    private val condition: Requirement?
    var invalidateRequirement: ChatMessageRequirement? = null
    private val messages: List<String>

    constructor(vararg message: String) {
        this.condition = null
        this.messages = message.toList()
    }

    constructor(condition: Requirement, vararg message: String) {
        require(condition != null) { "Condition cannot be null" }
        this.condition = condition
        this.messages = message.toList()
    }

    override fun check(): Boolean {
        return hasReceivedChatMessage
    }

    fun validateCondition(chatMessage: Chatbox.Message): Boolean {
        // TODO: Thing worked with MesBox?!?!
        if (chatMessage.type != Chatbox.Message.Type.SERVER &&
            chatMessage.type != Chatbox.Message.Type.UNKNOWN
        ) {
            return false
        }

        if (!hasReceivedChatMessage) {
            if (messages.any { chatMessage.message.contains(it) }) {
                if (condition == null || condition.check()) {
                    hasReceivedChatMessage = true
                    return true
                }
            }
        } else {
            invalidateRequirement?.let { requirement ->
                requirement.validateCondition(chatMessage)
                if (requirement.check()) {
                    requirement.hasReceivedChatMessage = false
                    hasReceivedChatMessage = false
                }
            }
        }
        return false
    }
}