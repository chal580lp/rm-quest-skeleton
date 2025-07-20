package com.recursive.quester.framework.requirements

import com.runemate.game.api.hybrid.local.Quest


class QuestRequirement private constructor(
    private val quest: Quest,
    private val requiredStatus: Quest.Status? = null,
    private val displayText: String? = null
) : AbstractRequirement() {

    override fun check(): Boolean {
        return when {

            else -> {
                val state = quest.status
                when (requiredStatus) {
                    else -> state == requiredStatus
                }
            }
        }
    }

    override fun getDisplayText(): String {
        if (!displayText.isNullOrEmpty()) {
            return displayText
        }
        return when {
            else -> "${requiredStatus.toString().toLowerCase().capitalize()} ${quest.name}"
        }
    }

    companion object {
        fun withStatus(quest: Quest, status: Quest.Status, displayText: String? = null) =
            QuestRequirement(quest, requiredStatus = status, displayText = displayText)

    }
}