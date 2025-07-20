package com.recursive.quester.framework.steps.interactions

import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.steps.ObjectStep
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.entities.GameObject
import java.util.regex.Pattern

sealed class ObjectInteraction : CommonInteraction<GameObject, ObjectStep>() {

    companion object {
        private val log = getLogger("ObjectInteraction")

        fun simple(action: String = "Interact") = SimpleInteraction(action)
        fun simple(action: Pattern) = SimpleInteractionPattern(action)
        fun useItemOnObject(item: ItemRequirement) = UseItemOnObject(item)
        fun multiAction(vararg items: ItemRequirement) = MultiAction(items.toList())

        fun determineStrategy(
            text: String?,
            requirements: List<Requirement>
        ): ObjectInteraction {
            val action = text?.lowercase()?.let { name ->
                when {
                    name.contains("lever") -> "Pull"
                    name.contains("door") -> "Open"
                    name.contains("gate") -> "Open"
                    name.contains("chest") -> "Open"
                    name.contains("search") -> "Search"
                    name.contains("investigate") -> "Investigate"
                    name.contains("remove") -> "Remove"
                    else -> "Interact"
                }
            } ?: "Interact"

            return simple(action)
        }
    }


    override fun addBaseSteps(step: ObjectStep) {
        addMovementStep(
            getPosition = { step.config.position },
            getEntity = { step.getClosestEntity() },
            visibilityCheck = { it.isVisible }
        )
        addFullVisibilityCheck(
            getEntity = { step.getClosestEntity() },
            getPosition = { step.config.position },
            entityName = "Object"
        )
    }

    data class SimpleInteraction(
        private val action: String = "Interact"
    ) : ObjectInteraction() {
        override fun execute(step: ObjectStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: ObjectStep) {
            addSimpleInteraction(
                getEntity = { step.getClosestEntity() },
                action = action
            )
        }
    }

    data class SimpleInteractionPattern(
        private val action: Pattern
    ) : ObjectInteraction() {
        override fun execute(step: ObjectStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: ObjectStep) {
            addPatternInteraction(
                getEntity = { step.getClosestEntity() },
                pattern = action
            )
        }
    }

    data class UseItemOnObject(
        private val item: ItemRequirement
    ) : ObjectInteraction() {
        override fun execute(step: ObjectStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: ObjectStep) {
            addItemInteraction(
                getEntity = { step.getClosestEntity() },
                item = item
            )
        }
    }

    data class MultiAction(
        private val items: List<ItemRequirement>
    ) : ObjectInteraction() {
        override fun execute(step: ObjectStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: ObjectStep) {
            addMultiItemInteraction(
                getEntity = { step.getClosestEntity() },
                items = items
            )
        }
    }
}