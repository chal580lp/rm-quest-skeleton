package com.recursive.quester.framework.requirements.conditional

import com.recursive.quester.framework.requirements.Requirement
import com.runemate.game.api.script.framework.AbstractBot


interface InitializableRequirement : Requirement {

    fun initialize(bot: AbstractBot)

    fun updateHandler()

    val conditions: List<Requirement>
}