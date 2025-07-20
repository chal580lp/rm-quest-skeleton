package com.recursive.quester.framework.steps.interactions

import com.runemate.game.api.hybrid.entities.details.Interactable

abstract class EntityInteraction<T : Interactable, S> : CommonInteraction<T, S>() {
    override fun execute(step: S): Boolean {
        if (steps.isEmpty()) {
            prepareSteps(step)
        }
        return execute()
    }

    override fun prepareSteps(step: S) {
        addBaseSteps(step)
        addSpecificSteps(step)
    }

    abstract override fun addSpecificSteps(step: S)
}