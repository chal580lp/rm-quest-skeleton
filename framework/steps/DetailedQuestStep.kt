package com.recursive.quester.framework.steps

import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.steps.builders.ConcreteDetailedStepBuilder
import com.runemate.game.api.hybrid.entities.GroundItem
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.GroundItems
import com.runemate.game.api.hybrid.region.Players

open class DetailedQuestStep(
    config: StepConfiguration,
    protected val detailedConfig: DetailedStepConfiguration  // Make protected,
) : QuestStep(config) {

    data class DetailedStepConfiguration(
        var hideRequirements: Boolean = false,
        var considerBankForItemHighlight: Boolean = false,
        val markedTiles: MutableList<Coordinate> = mutableListOf(),
        val tileHighlights: MutableMap<Coordinate, MutableSet<Int>> = mutableMapOf()
    )

    fun getPlayerLocation(): Coordinate? = Players.getLocal()?.position

    fun addTileMarker(coordinate: Coordinate) {
        detailedConfig.markedTiles.add(coordinate)
    }

    protected fun addItemTiles(requirements: Collection<Requirement>) {
        if (requirements.isEmpty()) return

        GroundItems.newQuery().results().forEach { groundItem ->
            for (requirement in requirements) {
                if (isValidRequirementForTileItem(requirement, groundItem)) {
                    val tile = groundItem.position ?: return@forEach
                    detailedConfig.tileHighlights
                        .getOrPut(tile) { mutableSetOf() }
                        .add(groundItem.id)
                    break
                }
            }
        }
    }

    private fun isValidRequirementForTileItem(
        requirement: Requirement,
        item: GroundItem
    ): Boolean = requirement is ItemRequirement &&
            requirement.isActualItem() &&
            requirement.getAllIds().contains(item.id)

    override fun processStep() {
        updateTileHighlights()
    }

    private fun updateTileHighlights() {
        detailedConfig.tileHighlights.clear()
        addItemTiles(config.requirements)
        addItemTiles(config.recommended)
    }

    override fun isStepComplete(): Boolean = false
    override fun execute(): Boolean = false

    companion object {
        fun builder() = ConcreteDetailedStepBuilder()

        internal fun create(
            config: StepConfiguration,
            detailedConfig: DetailedStepConfiguration
        ): DetailedQuestStep {
            return DetailedQuestStep(config, detailedConfig)
        }
    }
}