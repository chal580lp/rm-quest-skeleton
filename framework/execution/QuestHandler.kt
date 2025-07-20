package com.recursive.quester.framework.execution

import com.recursive.quester.BotContext
import com.recursive.quester.framework.skeleton.BasicQuestHelper
import com.recursive.quester.framework.steps.ConditionalStep
import com.recursive.quester.framework.steps.QuestStep
import com.recursive.quester.framework.util.getLogger

class QuestHandler(private val ctx: BotContext) {
    private val log = getLogger("QuestHandler")
    lateinit var currentQuest: BasicQuestHelper
    private val stateManager = QuestStateManager()
    private val stepExecutor = StepExecutor()
    var currentStep: QuestStep? = null

    fun getContext() = ctx

    fun setQuest(quest: BasicQuestHelper) {
        currentQuest = quest
        quest.init()
        stateManager.initializeQuest(quest)
    }

    fun inProgress(): Boolean {
        return currentQuest.getVar() > 0
    }

    fun canStartQuest(): Boolean {
        val generalReq = currentQuest.getGeneralRequirements()?.all { it.check() } ?: true
        val itemReq = currentQuest.getItemRequirements()?.all { it.check() } ?: true
        if (!generalReq) {
            log.warn("General requirements not met")
        } else if (!itemReq) {
            log.warn("Item requirements not met")
        }
        return generalReq && itemReq

    }

    fun getFailedRequirements(): List<String> {
        val failedRequirements = mutableListOf<String>()
        currentQuest.getGeneralRequirements()?.forEach {
            if (!it.check()) {
                failedRequirements.add(it.getDisplayText())
            }
        }
        currentQuest.getItemRequirements()?.forEach {
            if (!it.check()) {
                failedRequirements.add(it.getDisplayText())
            }
        }
        return failedRequirements
    }

    fun execute(): Boolean {
        if (!::currentQuest.isInitialized) {
            throw IllegalStateException("Quest not set")
        }

        try {
            currentStep = stateManager.getCurrentStep(currentQuest)
            return stepExecutor.execute(currentStep)
        } catch (e: Exception) {
            stateManager.handleFailure()
            return false
        }
    }

    fun getAllSteps(): List<QuestStep> {
        val allSteps = mutableListOf<QuestStep>()

        currentQuest.steps.values.forEach { step ->
            when (step) {
                is ConditionalStep -> {
                    allSteps.addAll(step.getAllSteps())
                }

                else -> allSteps.add(step)
            }
        }

        return allSteps.distinct()
    }

    fun isComplete(): Boolean =
        currentQuest.getVar() >= getMaxStepValue()

    fun getCurrentState() = stateManager.getState(currentQuest)

    fun getMaxStepValue() = stateManager.getMaxStepValue()
}