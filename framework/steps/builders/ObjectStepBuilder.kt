// builders/ObjectStepBuilder.kt
package com.recursive.quester.framework.steps.builders

import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.steps.ObjectStep
import com.recursive.quester.framework.steps.ObjectStepConfiguration
import com.recursive.quester.framework.steps.interactions.ObjectInteraction
import java.util.regex.Pattern

class ObjectStepBuilder : InteractiveStepBuilder<ObjectStepBuilder, ObjectStep>() {
    private val objectConfig = ObjectStepConfiguration()
    private var interaction: ObjectInteraction? = null

    fun forObject(id: Int, name: String? = null) = apply {
        this.entityId = id
        this.entityName = name
    }

    fun withAlternateObjects(vararg ids: Int) = apply {
        alternateIds.addAll(ids.toSet())
    }

    fun showingAllInArea(show: Boolean = true) = apply {
        objectConfig.showAllInArea = show
    }

    fun withMaxDistance(distance: Int) = apply {
        objectConfig.maxObjectDistance = distance
        objectConfig.maxRenderDistance = distance
    }

    fun withAction(action: String) = apply {
        interaction = ObjectInteraction.simple(action)
    }

    fun withAction(pattern: Pattern) = apply {
        interaction = ObjectInteraction.simple(pattern)
    }

    fun withItemUse(item: ItemRequirement) = apply {
        interaction = ObjectInteraction.useItemOnObject(item)
    }

    fun withMultipleActions(vararg items: ItemRequirement) = apply {
        interaction = ObjectInteraction.multiAction(*items)
    }

    fun withCustomInteraction(interaction: ObjectInteraction) = apply {
        this.interaction = interaction
    }

    override fun build(): ObjectStep {
        requireNotNull(entityId) { "Object ID must be specified" }
        return ObjectStep.create(
            config = config,
            detailedConfig = detailedConfig,
            identifier = createIdentifier(),
            objectConfig = objectConfig,
            interaction = interaction
        ).also(::applyDialogSteps)
    }
}