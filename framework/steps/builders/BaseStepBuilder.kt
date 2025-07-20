package com.recursive.quester.framework.steps.builders

import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.steps.QuestStep
import com.runemate.game.api.hybrid.location.Coordinate

abstract class BaseStepBuilder<SELF : BaseStepBuilder<SELF, T>, T : QuestStep> : StepBuilder<SELF, T> {
    protected val config = QuestStep.StepConfiguration()
    protected val dialogSteps = mutableListOf<String>()
    protected var teleport: Any? = null

    // Need this to handle proper type returns
    @Suppress("UNCHECKED_CAST")
    protected fun self(): SELF = this as SELF

    override fun withText(text: String): SELF = self().apply {
        config.text = text
    }

    override fun requiring(vararg reqs: Requirement): SELF = self().apply {
        config.requirements.addAll(reqs)
    }

    override fun recommending(vararg recs: Requirement): SELF = self().apply {
        config.recommended.addAll(recs)
    }

    override fun at(coordinate: Coordinate): SELF = self().apply {
        config.position = coordinate
    }

    override fun at(x: Int, y: Int, z: Int): SELF = self().apply {
        config.position = Coordinate(x, y, z)
    }

    override fun lockable(isLockable: Boolean): SELF = self().apply {
        config.isLockable = isLockable
    }

    override fun blocking(blocks: Boolean): SELF = self().apply {
        config.blocker = blocks
    }

    override fun allowingInCutscene(allow: Boolean): SELF = self().apply {
        config.allowInCutscene = allow
    }

    override fun withLockingCondition(condition: Requirement): SELF = self().apply {
        config.lockingCondition = condition
    }

    override fun withDialogSteps(vararg steps: String): SELF = self().apply {
        dialogSteps.addAll(steps)
    }

    override fun withTeleport(teleport: Any): SELF = self().apply {
        this.teleport = teleport
    }
}