package com.recursive.quester.framework.steps

import com.recursive.quester.framework.interact.NpcDialog
import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.steps.choice.DialogChoiceStep
import com.recursive.quester.framework.steps.choice.DialogChoiceSteps
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.script.framework.listeners.ChatboxListener
import com.runemate.game.api.script.framework.listeners.events.MessageEvent
import java.util.regex.Pattern

abstract class QuestStep(
    val config: StepConfiguration
) : ChatboxListener {
    // Data class for configuration
    data class StepConfiguration(
        var text: String? = null,
        val requirements: MutableList<Requirement> = mutableListOf(),
        val recommended: MutableList<Requirement> = mutableListOf(),
        var position: Coordinate? = null,
        var isLockable: Boolean = false,
        var blocker: Boolean = false,
        var unlockable: Boolean = true,
        var allowInCutscene: Boolean = false,
        var lockingCondition: Requirement? = null
    )

    val isLockable: Boolean
        get() = config.isLockable

    var blocker: Boolean
        get() = config.blocker
        set(value) {
            config.blocker = value
        }

    // If you need to set isLockable
    fun setLockable(value: Boolean) {
        config.isLockable = value
    }

    // Builder for base configuration
    abstract class BaseBuilder<T : BaseBuilder<T>> {
        protected val config = StepConfiguration()
        protected val dialogSteps = mutableListOf<String>()  // Add this
        protected var teleport: Any? = null  // Add this


        fun withText(text: String) = apply { config.text = text } as T
        fun requiring(vararg reqs: Requirement) = apply { config.requirements.addAll(reqs) } as T
        fun recommending(vararg recs: Requirement) = apply { config.recommended.addAll(recs) } as T
        fun at(coordinate: Coordinate) = apply { config.position = coordinate } as T
        fun at(x: Int, y: Int, z: Int) = apply { config.position = Coordinate(x, y, z) } as T
        fun lockable(isLockable: Boolean = true) = apply { config.isLockable = isLockable } as T
        fun blocking(blocks: Boolean = true) = apply { config.blocker = blocks } as T
        fun allowingInCutscene(allow: Boolean = true) = apply { config.allowInCutscene = allow } as T
        fun withLockingCondition(condition: Requirement) = apply { config.lockingCondition = condition } as T

        abstract fun build(): QuestStep
    }

    val substeps = mutableListOf<QuestStep>()
    protected var started = false
    protected var inCutscene = false
    protected var locked = false

    val npcDialog = NpcDialog()
    val dialogChoiceSteps = DialogChoiceSteps()
    var lastDialogSeen = ""

    // Dialog management
    fun addDialogStep(choice: String) {
        dialogChoiceSteps.addChoice(DialogChoiceStep(choice))
    }

    open fun addDialogSteps(vararg choices: String) {
        choices.forEach { addDialogStep(it) }
    }

    fun addDialogStep(pattern: Pattern) {
        dialogChoiceSteps.addChoice(DialogChoiceStep(pattern))
    }

    fun addDialogStep(id: Int, choice: String) {
        dialogChoiceSteps.addChoice(DialogChoiceStep(id, choice))
    }

    fun resetDialogSteps() {
        dialogChoiceSteps.resetChoices()
    }

    // Message handling
    override fun onMessageReceived(event: MessageEvent?) {
        if (event?.type == Chatbox.Message.Type.SERVER) {
            lastDialogSeen = event.message
        }
    }

    // Step management
    open fun getActiveStep(): QuestStep = this

    fun addSubSteps(vararg steps: QuestStep) {
        substeps.addAll(steps)
    }

    fun checkRequirements(): Boolean = config.requirements.all { it.check() }

    fun isLocked(): Boolean {
        val autoLocked = config.lockingCondition?.check() == true
        config.unlockable = !autoLocked
        if (autoLocked) locked = true
        return locked
    }

    // Abstract methods that must be implemented by subclasses
    abstract fun processStep()
    abstract fun isStepComplete(): Boolean
    abstract fun execute(): Boolean

    // Virtual methods that can be overridden
    open fun startUp() {
        started = true
    }

    open fun shutDown() {
        started = false
    }

    open fun shouldExecute(): Boolean = false

    protected fun enteredCutscene() {
        inCutscene = true
    }

    protected fun leftCutscene() {
        inCutscene = false
    }
}