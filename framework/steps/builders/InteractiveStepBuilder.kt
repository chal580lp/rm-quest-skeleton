package com.recursive.quester.framework.steps.builders

import com.recursive.quester.framework.steps.DetailedQuestStep
import com.recursive.quester.framework.steps.Identifier

abstract class InteractiveStepBuilder<SELF : InteractiveStepBuilder<SELF, T>, T : DetailedQuestStep> :
    DetailedStepBuilder<SELF, T>() {

    protected var entityId: Int? = null
    protected var entityName: String? = null
    protected val alternateIds = mutableListOf<Int>()

    protected fun createIdentifier() = Identifier(entityName, entityId, alternateIds)

    // Common build functionality moved here
    protected fun applyDialogSteps(step: T) {
        dialogSteps.forEach { step.addDialogStep(it) }
    }
}