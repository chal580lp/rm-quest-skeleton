package com.recursive.quester.framework.requirements.conditional

import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.requirements.util.LogicType
import com.runemate.game.api.script.framework.AbstractBot

abstract class ConditionForStep : InitializableRequirement {
    var hasPassed: Boolean = false
    var onlyNeedToPassOnce: Boolean = false
    var logicType: LogicType? = null
    override val conditions: MutableList<Requirement> = mutableListOf()

    abstract override fun check(): Boolean

    override fun initialize(bot: AbstractBot) {
        conditions.filterIsInstance<InitializableRequirement>().forEach { it.initialize(bot) }
    }

    override fun updateHandler() {
        conditions.filterIsInstance<InitializableRequirement>().forEach { it.updateHandler() }
    }

    override fun getDisplayText(): String { // conditions don't need display text (yet?)
        return ""
    }


}