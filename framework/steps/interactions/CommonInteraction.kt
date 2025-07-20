package com.recursive.quester.framework.steps.interactions

import com.recursive.quester.Traverser
import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.entities.details.Interactable
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.Players
import java.util.regex.Pattern

interface StepPreparation<T, S> {
    fun prepareSteps(step: S)
    fun addBaseSteps(step: S)
}

abstract class CommonInteraction<T : Interactable, S> : BaseInteraction<T>(), StepPreparation<T, S> {
    protected val log = getLogger("CommonInteraction")

    final override fun execute(): Boolean = executeSteps()
    abstract fun execute(step: S): Boolean

    // Common base step methods
    protected fun addMovementStep(
        getPosition: () -> Coordinate?,
        getEntity: () -> T?,
        visibilityCheck: (T) -> Boolean,
        minDistance: Int = 15
    ) {
        getPosition()?.let { position ->
            addStep(
                "Moving to location",
                validate = {
                    getPosition()?.let { pos ->
                        pos.distanceTo(Players.getLocal()) >= minDistance
                    } == true || getEntity()?.let(visibilityCheck) != true
                },
                "Unable to find target",
                execute = { Traverser.navigateTo(position) }
            )
        }
    }

    protected fun addContinueDialogStep() {
        addStep(
            "Continuing dialog",
            validate = { ChatDialog.isOpen() },
            execute = {
                ChatDialog.getContinue()?.select()
                true
            }
        )
    }

    protected fun addScanStep(
        getEntity: () -> T?,
        scanAction: () -> Boolean,
        entityName: String
    ) {
        addStep(
            "Scanning for $entityName",
            validate = { getEntity() == null },
            "$entityName is null",
            execute = scanAction
        )
    }

    // Common visibility and targeting methods
    protected fun addFullVisibilityCheck(
        getEntity: () -> T?,
        getPosition: () -> Coordinate?,
        entityName: String
    ) {
        // Add basic scan
        addScanStep(getEntity, { getEntity() != null }, entityName)

        // Add visibility step if position exists
        if (getPosition() != null) {
            addStep(
                "Checking $entityName visibility",
                validate = { getEntity()?.visibility != 100.0 },
                "$entityName visibility is not 100%",
                execute = {
                    adjustCamera(getEntity())
                    true
                }
            )
        }
    }

    // Common interaction patterns
    protected fun addSimpleInteraction(
        getEntity: () -> T?,
        action: String
    ) {
        addStep(
            "Attempting to $action",
            validate = { getEntity() != null },
            execute = {
                getEntity()?.interact(action) ?: false
            }
        )
    }

    protected fun addPatternInteraction(
        getEntity: () -> T?,
        pattern: Pattern
    ) {
        addStep(
            "Attempting to interact using pattern $pattern",
            validate = { getEntity() != null },
            execute = {
                getEntity()?.interact(pattern) ?: false
            }
        )
    }

    // Item usage patterns
    protected fun addItemInteraction(
        getEntity: () -> T?,
        item: ItemRequirement,
        inDialogCheck: Boolean = true
    ) {
        addItemRequirementStep(item)

        addStep(
            "Using ${item.name} on target",
            validate = { getEntity() != null && (!inDialogCheck || !ChatDialog.isOpen()) },
            execute = {
                getEntity()?.let { entity ->
                    useItemOn(item, entity)
                } ?: false
            }
        )
    }

    protected fun addMultiItemInteraction(
        getEntity: () -> T?,
        items: List<ItemRequirement>
    ) {
        items.forEach { item ->
            addItemRequirementStep(item)

            addStep(
                "Using ${item.name} on target",
                validate = {
                    itemCheck(item) && getEntity() != null
                },
                execute = {
                    getEntity()?.let { entity ->
                        useItemOn(item, entity)
                    } ?: false
                }
            )
        }
    }

    // Base implementation of StepPreparation
    override fun prepareSteps(step: S) {
        if (steps.isEmpty()) {
            addBaseSteps(step)
            addSpecificSteps(step)
        }
    }

    protected abstract fun addSpecificSteps(step: S)
}