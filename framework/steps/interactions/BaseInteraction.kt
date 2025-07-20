package com.recursive.quester.framework.steps.interactions

import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.entities.details.Interactable
import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.input.direct.DirectInput
import com.runemate.game.api.hybrid.local.Camera
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution

abstract class BaseInteraction<T : Interactable> {
    protected val steps = mutableListOf<InteractionStep>()
    protected var currentStepIndex = -1
    private val observers = mutableListOf<(List<InteractionStep>, Int) -> Unit>()

    // Common utility functions for all interaction types
    protected companion object {
        private val log = getLogger("BaseInteraction")
        fun itemCheck(item: ItemRequirement) = item.check()

        fun entityVisible(entity: Interactable?) = entity?.isVisible == true

        fun adjustCamera(entity: Interactable?): Boolean {
            if (entity !is Locatable) return false
            if (!entity.isVisible) {
                Camera.turnTo(entity)
            }
            return entity.isVisible
        }

        fun useItemOn(item: ItemRequirement, target: Interactable): Boolean {
            return if (itemCheck(item)) {
                val spriteItem = Inventory.newQuery().ids(item.id).results().firstOrNull()
                if (spriteItem == null) {
                    log.error("Item ${item.id} not found in inventory")
                    return false
                }
                DirectInput.sendItemUseOn(spriteItem, target)
                Execution.delayUntil(
                    { ChatDialog.isOpen() || !spriteItem.isValid },
                    { Players.getLocal()?.isIdle == false },
                    1600
                )
            } else false
        }
    }

    fun removeObserver(observer: (List<InteractionStep>, Int) -> Unit) {
        observers.remove(observer)
    }

    // Common helper methods for adding standard steps
    protected fun addVisibilityStep(getEntity: () -> T?) {
        addStep(
            "Target not visible on screen",
            validate = {
                val entity = getEntity()
                entity != null && !entityVisible(entity)
            },
            execute = {
                adjustCamera(getEntity())
            }
        )
    }

    protected fun addItemRequirementStep(item: ItemRequirement) {
        addStep(
            "Looking for ${item.name}",
            validate = { !itemCheck(item) },
            execute = { itemCheck(item) }
        )
    }

    protected fun addBasicInteractionStep(
        action: String,
        getEntity: () -> T?,
        interact: (T, String) -> Boolean
    ) {
        addStep(
            "Attempting to $action",
            validate = { getEntity() != null },
            execute = {
                getEntity()?.let { entity -> interact(entity, action) } ?: false
            }
        )
    }

    fun addStep(
        action: String,
        validate: () -> Boolean,
        reason: String? = null,
        execute: () -> Boolean
    ) {
        steps.add(InteractionStep(action, validate, reason, execute))
        notifyObservers()
    }

    protected fun executeSteps(): Boolean {
        steps.forEachIndexed { index, step ->
            if (step.validate()) {
                currentStepIndex = index
                notifyObservers()

                if (!step.execute()) return false
            }
        }
        return true
    }

    fun addObserver(observer: (List<InteractionStep>, Int) -> Unit) {
        observers.add(observer)
        observer(steps, currentStepIndex)
    }

    private fun notifyObservers() {
        observers.forEach { it(steps, currentStepIndex) }
    }

    abstract fun execute(): Boolean

    fun getCurrentStep() = currentStepIndex.takeIf { it >= 0 }?.let { steps.getOrNull(it) }
    fun getAllSteps() = steps.toList()

    fun reset() {
        currentStepIndex = -1
        notifyObservers()
    }
}