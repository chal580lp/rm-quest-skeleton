package com.recursive.quester.framework.execution

import com.recursive.quester.framework.skeleton.BasicQuestHelper
import com.recursive.quester.framework.steps.QuestStep

class QuestStateManager {
    private var questSteps: Map<Int, QuestStep> = emptyMap()
    private var currentStep: QuestStep? = null
    private var currentVarValue: Int = 0
    private var consecutiveFailures = 0
    private var lastAttemptTime = 0L

    private companion object {
        const val MAX_FAILURES = 3
        const val FAILURE_RESET_TIME = 10000L
    }

    fun initializeQuest(quest: BasicQuestHelper) {
        questSteps = quest.loadSteps()
        currentVarValue = quest.getVar()
        updateCurrentStep(currentVarValue)
    }

    fun getCurrentStep(quest: BasicQuestHelper): QuestStep? {
        currentVarValue = quest.getVar()
        updateCurrentStep(currentVarValue)
        return currentStep
    }

    private fun updateCurrentStep(progress: Int) {
        val newStep = questSteps[progress]?.getActiveStep()
        if (newStep != currentStep) {
            currentStep?.shutDown()
            currentStep = newStep
            newStep?.startUp()
            resetFailures()
        }
    }

    fun handleFailure() {
        consecutiveFailures++
        lastAttemptTime = System.currentTimeMillis()

        if (consecutiveFailures >= MAX_FAILURES) {
            // Instead of throwing, we could handle this differently
            consecutiveFailures = 0
        }
    }

    private fun resetFailures() {
        if (System.currentTimeMillis() - lastAttemptTime > FAILURE_RESET_TIME) {
            consecutiveFailures = 0
        }
    }

    fun getMaxStepValue(): Int = questSteps.keys.maxOrNull() ?: 0

    fun getState(quest: BasicQuestHelper) = QuestState(
        questName = quest::class.simpleName ?: "Unknown Quest",
        currentStep = currentStep?.toString() ?: "No current step",
        progress = currentVarValue,
        isComplete = currentVarValue >= getMaxStepValue(),
        failureCount = consecutiveFailures
    )
}