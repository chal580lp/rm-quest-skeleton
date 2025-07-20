package com.recursive.quester.framework.steps.builders

import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.steps.QuestStep
import com.runemate.game.api.hybrid.location.Coordinate

interface StepBuilder<SELF : StepBuilder<SELF, T>, T : QuestStep> {
    fun build(): T
    fun withText(text: String): SELF
    fun requiring(vararg reqs: Requirement): SELF
    fun recommending(vararg recs: Requirement): SELF
    fun at(coordinate: Coordinate): SELF
    fun at(x: Int, y: Int, z: Int): SELF
    fun lockable(isLockable: Boolean = true): SELF
    fun blocking(blocks: Boolean = true): SELF
    fun allowingInCutscene(allow: Boolean = true): SELF
    fun withLockingCondition(condition: Requirement): SELF
    fun withDialogSteps(vararg steps: String): SELF
    fun withTeleport(teleport: Any): SELF
}
