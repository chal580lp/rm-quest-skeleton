package com.recursive.quester.framework.steps.builders

import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.steps.NpcStep
import com.recursive.quester.framework.steps.NpcStepConfiguration
import com.recursive.quester.framework.steps.interactions.NpcInteraction

class NpcStepBuilder : InteractiveStepBuilder<NpcStepBuilder, NpcStep>() {
    private val npcConfig = NpcStepConfiguration()
    private var interaction: NpcInteraction? = NpcInteraction.talkTo()

    fun forNpc(id: Int, name: String? = null) = apply {
        this.entityId = id
        this.entityName = name
    }

    fun forNpc(name: String) = apply {
        this.entityName = name
    }

    fun withAlternateNpcs(vararg ids: Int) = apply {
        alternateIds.addAll(ids.toSet())
    }

    fun withRoamRange(range: Int) = apply {
        npcConfig.maxRoamRange = range
    }

    fun focusedOnPlayer(focused: Boolean = true) = apply {
        npcConfig.mustBeFocusedOnPlayer = focused
    }

    fun withAction(action: String) = apply {
        interaction = NpcInteraction.simple(action)
    }

    fun withTalkTo(action: String = "Talk-to") = apply {
        interaction = NpcInteraction.talkTo(action)
    }

    fun withCustomInteraction(interaction: NpcInteraction) = apply {
        this.interaction = interaction
    }

    fun withInteraction(interaction: NpcInteraction) = apply {
        this.interaction = interaction
    }

    fun useItemOn(itemRequirement: ItemRequirement) = apply {
        this.interaction = NpcInteraction.UseItemOnNpc(itemRequirement)
    }

    override fun build(): NpcStep = NpcStep.create(
        config = config,
        detailedConfig = detailedConfig,
        identifier = createIdentifier(),
        npcConfig = npcConfig,
        interaction = interaction
    ).also(::applyDialogSteps)
}