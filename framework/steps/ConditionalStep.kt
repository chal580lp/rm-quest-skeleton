package com.recursive.quester.framework.steps

import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.script.framework.listeners.ChatboxListener
import com.runemate.game.api.script.framework.listeners.EngineListener
import com.runemate.game.api.script.framework.listeners.events.EngineEvent

class ConditionalStep private constructor(
    config: QuestStep.StepConfiguration,
    private val defaultStep: QuestStep,
    private val conditionalConfig: ConditionalStepConfiguration
) : QuestStep(config), OwnerStep, ChatboxListener, EngineListener {
    private val log = getLogger("ConditionalStep")

    data class ConditionalStepConfiguration(
        var checkAllChildStepsOnListenerCall: Boolean = false,
        val steps: LinkedHashMap<Requirement?, QuestStep> = LinkedHashMap()
    )

    var currentStep: QuestStep? = null

    class Builder : BaseBuilder<Builder>() {
        private var defaultStep: QuestStep? = null
        private val conditionalConfig = ConditionalStepConfiguration()
        private val stepsList = mutableListOf<Triple<Requirement?, QuestStep, Boolean>>()

        fun withDefaultStep(step: QuestStep) = apply {
            this.defaultStep = step
        }

        fun addStep(requirement: Requirement?, step: QuestStep, isLockable: Boolean = false) = apply {
            stepsList.add(Triple(requirement, step, isLockable))
        }

        fun checkingAllChildSteps(check: Boolean = true) = apply {
            conditionalConfig.checkAllChildStepsOnListenerCall = check
        }

        override fun build(): ConditionalStep {
            requireNotNull(defaultStep) { "Default step must be specified" }

            return ConditionalStep(config, defaultStep!!, conditionalConfig).apply {
                stepsList.forEach { (requirement, step, isLockable) ->
                    addStep(requirement, step, isLockable)
                }
            }
        }
    }

    fun addStep(requirement: Requirement?, step: QuestStep, isLockable: Boolean = false) {
        step.setLockable(isLockable)  // Use the new setter method
        conditionalConfig.steps[requirement] = step
    }

    override fun processStep() {
        updateSteps()
        currentStep?.processStep()
    }

    private fun updateSteps() {
        var lastPossibleStep: QuestStep? = null
        var chosenStep: QuestStep? = null
        var stepReason: String = ""
        var checkedStepCount = 0

        for ((requirement, step) in conditionalConfig.steps) {
            checkedStepCount++

            when {
                requirement == null || (requirement.check() && !step.isLocked()) -> {
                    chosenStep = step
                    stepReason = if (requirement == null) "default step" else "requirement met"
                    break
                }

                step.blocker && step.isLocked() -> {
                    chosenStep = lastPossibleStep
                    stepReason = "blocker locked, using last possible"
                    break
                }

                !step.isLocked() -> {
                    lastPossibleStep = step
                }
            }
        }

        // If no step was chosen during the loop
        if (chosenStep == null) {
            chosenStep = if (!defaultStep.isLocked()) {
                stepReason = "falling back to default"
                defaultStep
            } else {
                stepReason = "using last possible"
                lastPossibleStep
            }
        }

        // Log the final decision
        log.debug(
            "Step [${conditionalConfig.steps.size - checkedStepCount}/${conditionalConfig.steps.size}] " +
                    "Current: ${currentStep?.config?.text ?: "none"} -> " +
                    "Chosen: ${chosenStep?.config?.text ?: "none"} ($stepReason)"
        )

        // Start the chosen step
        chosenStep?.let { startUpStep(it) }
    }

    private fun startUpStep(step: QuestStep) {
        if (step == currentStep) return

        log.debug("Transitioning step: ${currentStep?.config?.text ?: "none"} -> ${step.config.text}")
        shutDownStep()
        step.processStep()
        currentStep = step
        log.debug("Setting current step to: ${currentStep?.config?.text}")
    }

    private fun shutDownStep() {
        currentStep?.let { step ->
            if (step is EngineListener) {
                // Handle engine listener cleanup
            }
            if (step is ChatboxListener) {
                // Handle chatbox listener cleanup
            }
            step.shutDown()
        }
        currentStep = null
    }

    override fun isStepComplete(): Boolean = currentStep?.isStepComplete() ?: false

    override fun shouldExecute(): Boolean {
        updateSteps()
        return currentStep?.shouldExecute() ?: true
    }

    override fun execute(): Boolean {
        log.debug("Executing ConditionalStep: ${config.text}")
        updateSteps()
        return currentStep?.execute() ?: false
    }

    override fun getActiveStep(): QuestStep {
        updateSteps()
        return currentStep?.getActiveStep() ?: defaultStep
    }

    override fun getSteps(): Collection<QuestStep> = conditionalConfig.steps.values

    override fun onEngineEvent(event: EngineEvent) {
        if (event.type != EngineEvent.Type.SERVER_TICK) return

        updateSteps()
        currentStep?.let { step ->
            if (step is EngineListener) {
                step.onEngineEvent(event)
            }
        }

        handleChildStepValidation { step ->
            if (step is EngineListener) {
                step.onEngineEvent(event)
            }
        }
    }

    private fun handleChildStepValidation(action: (QuestStep) -> Unit) {
        if (conditionalConfig.checkAllChildStepsOnListenerCall) {
            conditionalConfig.steps.values
                .filterIsInstance<ConditionalStep>()
                .forEach(action)
        }
    }

    companion object {
        fun builder() = Builder()
    }

    fun getAllSteps(): List<QuestStep> {
        val allSteps = mutableListOf<QuestStep>()
        allSteps.add(defaultStep)
        conditionalConfig.steps.values.forEach { step ->
            allSteps.add(step)
        }
        return allSteps
    }
}