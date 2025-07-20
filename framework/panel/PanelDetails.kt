package com.recursive.quester.framework.panel

import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.requirements.conditional.Conditions
import com.recursive.quester.framework.requirements.util.LogicType
import com.recursive.quester.framework.steps.QuestStep


data class PanelDetails(
    val header: String,
    val steps: List<QuestStep>,
    val requirements: List<Requirement> = emptyList(),
    val recommended: List<Requirement> = emptyList(),
    var lockingQuestSteps: QuestStep? = null,
    var hideCondition: Requirement? = null,
    var vars: List<Int> = emptyList()
) {
    fun setDisplayCondition(req: Requirement) {
        hideCondition = Conditions(LogicType.NOR, req)
    }

    fun setVars(vararg vars: Int) {
        this.vars = vars.toList()
    }

    fun setLockingStep(lockingStep: QuestStep) {
        lockingQuestSteps = lockingStep
    }

    fun contains(currentStep: QuestStep): Boolean = when {
        steps.contains(currentStep) -> true
        else -> steps
            .filterNotNull()
            .flatMap { it.substeps }
            .any { step -> containsSubStep(currentStep, step) }
    }

    private fun containsSubStep(currentStep: QuestStep, check: QuestStep): Boolean = when {
        currentStep.substeps.contains(check) || currentStep == check -> true
        else -> currentStep.substeps.any { containsSubStep(it, check) }
    }

    companion object {
        // Factory methods for common creation patterns
        fun create(header: String, step: QuestStep, recommended: Requirement? = null) =
            PanelDetails(
                header = header,
                steps = listOf(step),
                recommended = recommended?.let { listOf(it) } ?: emptyList()
            )

        fun create(header: String, steps: List<QuestStep>) =
            PanelDetails(header = header, steps = steps)
    }
}