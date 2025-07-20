package com.recursive.quester.framework.steps

import com.recursive.quester.framework.extension.maxDistance
import com.recursive.quester.framework.steps.builders.NpcStepBuilder
import com.recursive.quester.framework.steps.interactions.NpcInteraction
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.region.Npcs

data class NpcStepConfiguration(
    var maxRoamRange: Int = 48,
    var mustBeFocusedOnPlayer: Boolean = false,
    val mustBeFocusedOnNpcs: MutableList<Int> = mutableListOf(),
)

class NpcStep private constructor(
    config: StepConfiguration,
    detailedConfig: DetailedStepConfiguration,
    identifier: Identifier,
    val npcConfig: NpcStepConfiguration,
    interaction: NpcInteraction?
) : InteractiveDetailedStep<Npc, NpcInteraction>(
    config,
    detailedConfig,
    identifier,
    interaction,
    npcConfig.maxRoamRange
) {
    private val log = getLogger("NpcStep")

    override fun entityPassesChecks(entity: Npc): Boolean =
        identifier.id == null ||
                entity.id == identifier.id ||
                (identifier.name != null && entity.definition?.name?.equals(identifier.name, ignoreCase = true) == true)

    override fun updateEntities() {
        entities.clear()
        _closestEntity = null
        val playerPosition = getPlayerLocation() ?: return

        Npcs.newQuery()
            .maxDistance(maxDistance)
            .filter { entityPassesChecks(it) }
            .results()
            .forEach { npc ->
                entities.add(npc)
                val distance = npc.distanceTo(playerPosition)
                if (_closestEntity == null || distance < (_closestEntity?.distanceTo(playerPosition)
                        ?: Double.MAX_VALUE)
                ) {
                    _closestEntity = npc
                }
            }
    }

    override fun execute(): Boolean {
        if (!shouldExecute()) return false

        updateEntities()
        if (!checkInteractionCooldown()) return false

        val interactionStrategy = interaction ?: NpcInteraction.determineStrategy(this)
        val result = interactionStrategy.execute(this)

        if (result) {
            updateInteractionTime()
        }
        return result
    }

    companion object {
        fun builder() = NpcStepBuilder()

        fun create(
            config: StepConfiguration,
            detailedConfig: DetailedStepConfiguration,
            identifier: Identifier,
            npcConfig: NpcStepConfiguration,
            interaction: NpcInteraction?
        ): NpcStep {
            return NpcStep(config, detailedConfig, identifier, npcConfig, interaction)
        }
    }
}
