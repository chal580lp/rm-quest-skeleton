package com.recursive.quester.framework.execution

import com.recursive.quester.framework.steps.QuestStep
import com.recursive.quester.framework.util.Delay
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.RuneScape
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog

class StepExecutor {
    private val log = getLogger("StepExecutor")

    sealed class ExecutionResult {
        object Success : ExecutionResult()
        data class Failure(val reason: String) : ExecutionResult()
    }

    fun execute(step: QuestStep?): Boolean {
        if (step == null) return false.also { log.warn("Step is null") }

        return when (val result = validateStep(step)) {
            is ExecutionResult.Success -> executeStep(step)
            is ExecutionResult.Failure -> false.also { log.warn(result.reason) }
        }
    }

    private fun validateStep(step: QuestStep): ExecutionResult {
        return when {
            step.isLocked() -> ExecutionResult.Failure("Step is locked")
            !step.checkRequirements() -> ExecutionResult.Failure("Requirements not met")
            !step.shouldExecute() -> ExecutionResult.Failure("Should not execute")
            else -> ExecutionResult.Success
        }
    }

    private fun executeStep(step: QuestStep): Boolean {
        log.info("Executing step: ${step.config.text}")

        return when {
            step.isStepComplete() -> true.also { log.debug("Step already complete") }
            RuneScape.isCutscenePlaying() -> handleCutscene()
            else -> step.execute()
        }
    }

    private fun handleCutscene(): Boolean {
        log.info("Cutscene playing, managing dialog")
        return Delay.until(
            { ChatDialog.getContinue()?.select() == true },
            failCondition = { RuneScape.isCutscenePlaying() }
        ).also { log.info(if (it) "Dialog continued" else "Cutscene continuing") }
    }
}