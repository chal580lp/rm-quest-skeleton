package com.recursive.quester.framework.steps.builders

import com.recursive.quester.framework.steps.DetailedQuestStep
import com.runemate.game.api.hybrid.location.Coordinate

open class DetailedStepBuilder<SELF : DetailedStepBuilder<SELF, T>, T : DetailedQuestStep> :
    BaseStepBuilder<SELF, T>() {

    protected val detailedConfig = DetailedQuestStep.DetailedStepConfiguration()

    fun hidingRequirements(hide: Boolean = true): SELF = self().apply {
        detailedConfig.hideRequirements = hide
    }

    fun consideringBank(consider: Boolean = true): SELF = self().apply {
        detailedConfig.considerBankForItemHighlight = consider
    }

    fun withMarkedTiles(vararg tiles: Coordinate): SELF = self().apply {
        detailedConfig.markedTiles.addAll(tiles)
    }

    fun withTileHighlight(tile: Coordinate, itemId: Int): SELF = self().apply {
        detailedConfig.tileHighlights
            .getOrPut(tile) { mutableSetOf() }
            .add(itemId)
    }

    open fun withDialogStep(step: String): SELF = self().apply {
        dialogSteps.add(step)
    }

    override fun withDialogSteps(vararg steps: String): SELF = self().apply {
        dialogSteps.addAll(steps)
    }

    override fun withTeleport(teleport: Any): SELF = self().apply {
        this.teleport = teleport
    }

    @Suppress("UNCHECKED_CAST")
    override fun build(): T {
        return DetailedQuestStep.create(
            config = config,
            detailedConfig = detailedConfig
        ) as T
    }
}

class ConcreteDetailedStepBuilder : DetailedStepBuilder<ConcreteDetailedStepBuilder, DetailedQuestStep>()
