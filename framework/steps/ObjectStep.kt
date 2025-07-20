package com.recursive.quester.framework.steps

import com.recursive.quester.framework.extension.maxDistance
import com.recursive.quester.framework.steps.builders.ObjectStepBuilder
import com.recursive.quester.framework.steps.interactions.ObjectInteraction
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.region.GameObjects

data class ObjectStepConfiguration(
    var maxObjectDistance: Int = 50,
    var maxRenderDistance: Int = 50,
    var showAllInArea: Boolean = false,
)

class ObjectStep private constructor(
    config: StepConfiguration,
    detailedConfig: DetailedStepConfiguration,
    identifier: Identifier,
    val objectConfig: ObjectStepConfiguration,
    interaction: ObjectInteraction?
) : InteractiveDetailedStep<GameObject, ObjectInteraction>(
    config,
    detailedConfig,
    identifier,
    interaction,
    objectConfig.maxObjectDistance
) {
    private val log = getLogger("ObjectStep")

    override fun entityPassesChecks(entity: GameObject): Boolean =
        identifier.id == null ||
                entity.id == identifier.id ||
                (identifier.name != null && entity.definition?.name?.equals(identifier.name, ignoreCase = true) == true)

    override fun updateEntities() {
        entities.clear()
        _closestEntity = null
        val playerPosition = getPlayerLocation() ?: return

        val query = GameObjects.newQuery().maxDistance(maxDistance)

        val results = when {
            config.position != null && !objectConfig.showAllInArea -> {
                query.on(config.position).filter { entityPassesChecks(it) }.results()
            }

            else -> {
                query.filter { entityPassesChecks(it) }.results()
            }
        }

        results.forEach { obj ->
            entities.add(obj)
            val distance = obj.distanceTo(playerPosition)
            if (_closestEntity == null || distance < (_closestEntity?.distanceTo(playerPosition) ?: Double.MAX_VALUE)) {
                _closestEntity = obj
            }
        }
    }

    override fun execute(): Boolean {
        if (!shouldExecute()) {
            log.warn("Should not execute")
            return false
        }

        updateEntities()
        if (!checkInteractionCooldown()) {
            log.debug("Waiting for interaction cooldown")
            return false
        }

        val interactionStrategy = interaction ?: ObjectInteraction.determineStrategy(
            text = config.text,
            requirements = config.requirements
        )

        val result = interactionStrategy.execute(this)
        if (result) {
            updateInteractionTime()
        }
        log.debug("Interaction result: $result")
        return result
    }

    companion object {
        fun builder() = ObjectStepBuilder()

        fun create(
            config: StepConfiguration,
            detailedConfig: DetailedStepConfiguration,
            identifier: Identifier,
            objectConfig: ObjectStepConfiguration,
            interaction: ObjectInteraction?
        ): ObjectStep {
            return ObjectStep(config, detailedConfig, identifier, objectConfig, interaction)
        }
    }
}